package io.github.pedrohribeiross.lancecerto.shared.exception;

import com.jayway.jsonpath.JsonPath;
import io.github.pedrohribeiross.lancecerto.auction.Auction;
import io.github.pedrohribeiross.lancecerto.auction.AuctionRepository;
import io.github.pedrohribeiross.lancecerto.auction.AuctionStatus;
import io.github.pedrohribeiross.lancecerto.auction.dto.AuctionUpdateStatusRequest;
import io.github.pedrohribeiross.lancecerto.bid.dto.BidRequest;
import io.github.pedrohribeiross.lancecerto.category.Category;
import io.github.pedrohribeiross.lancecerto.category.CategoryRepository;
import io.github.pedrohribeiross.lancecerto.lot.Lot;
import io.github.pedrohribeiross.lancecerto.lot.LotRepository;
import io.github.pedrohribeiross.lancecerto.support.IntegrationTest;
import io.github.pedrohribeiross.lancecerto.support.fixtures.*;
import io.github.pedrohribeiross.lancecerto.support.security.Tokens;
import io.github.pedrohribeiross.lancecerto.user.User;
import io.github.pedrohribeiross.lancecerto.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

import static io.github.pedrohribeiross.lancecerto.support.web.ErrorResponseMatchers.errorResponse;
import static io.github.pedrohribeiross.lancecerto.support.web.ErrorResponseMatchers.hasOnlyErrorResponseFields;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ErrorResponseContractTest extends IntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private LotRepository lotRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should return a 400 response in the error envelope with field errors when the auction request is invalid")
    void shouldReturnBadRequestEnvelopeWithFieldErrorsWhenAuctionRequestIsInvalid() throws Exception {
        String token = Tokens.signedByAppKey().build();

        mockMvc.perform(withToken(post("/auctions"), token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(AuctionRequests.withTitle(""))))
                .andExpect(errorResponse(HttpStatus.BAD_REQUEST))
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors[?(@.field == 'title')]").exists());
    }

    @Test
    @DisplayName("Should serialize the timestamp as ISO-8601 text on both filter (401) and MVC (400) errors")
    void shouldSerializeTimestampAsIsoTextOnFilterAndMvcErrors() throws Exception {
        String unauthorizedBody = mockMvc.perform(post("/auctions"))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        String badRequestBody = mockMvc.perform(withToken(post("/auctions"), Tokens.signedByAppKey().build())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(AuctionRequests.withTitle(""))))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertIsoTimestamp(unauthorizedBody);
        assertIsoTimestamp(badRequestBody);
    }

    @Test
    @DisplayName("Should return a 400 response with the error envelope when the request body is invalid or malformed")
    void shouldReturnBadRequestWithErrorEnvelopeWhenRequestBodyIsInvalidOrMalformed() throws Exception {
        String token = Tokens.signedByAppKey().build();

        mockMvc.perform(withToken(post("/auctions"), token))
                .andExpect(errorResponse(HttpStatus.BAD_REQUEST))
                .andExpect(jsonPath("$.message").value("Corpo da requisição inválido ou malformado"))
                .andExpect(hasOnlyErrorResponseFields());
    }

    @Test
    @DisplayName("Should return a 400 response with the error envelope when the sort field is invalid")
    void shouldReturnBadRequestWithErrorEnvelopeWhenSortFieldIsInvalid() throws Exception {
        String token = Tokens.signedByAppKey().build();

        mockMvc.perform(withToken(get("/auctions?sort=invalidField"), token))
                .andExpect(errorResponse(HttpStatus.BAD_REQUEST))
                .andExpect(jsonPath("$.message").value("Não é possível ordenar pelo campo 'invalidField': campo inexistente"))
                .andExpect(hasOnlyErrorResponseFields());
    }

    @Test
    @DisplayName("Should return a 400 response with the error envelope when a path variable has an invalid type")
    void shouldReturnBadRequestWithErrorEnvelopeWhenPathVariableHasInvalidType() throws Exception {
        mockMvc.perform(get("/auctions/{id}", "nao-e-um-uuid"))
                .andExpect(errorResponse(HttpStatus.BAD_REQUEST))
                .andExpect(jsonPath("$.message").value("O parâmetro 'id' possui um valor inválido"))
                .andExpect(hasOnlyErrorResponseFields());
    }

    @Test
    @DisplayName("Should return a 404 response with the error envelope when the path variable value does not exist")
    void shouldReturnNotFoundWithErrorEnvelopeWhenPathVariableValueDoesNotExist() throws Exception {
        mockMvc.perform(get("/auctions/{id}", UUID.randomUUID().toString()))
                .andExpect(errorResponse(HttpStatus.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Recurso não encontrado: Leilão"))
                .andExpect(hasOnlyErrorResponseFields());
    }

    @Test
    @DisplayName("Should return a 409 response with the error envelope when the auction status transition is invalid")
    void shouldReturnConflictWithErrorEnvelopeWhenAuctionStatusTransitionIsInvalid() throws Exception {
        String token = Tokens.signedByAppKey().build();
        String auctionId = createAuction(token);

        // Arranjo: leva o leilão a um estado terminal
        changeAuctionStatus(token, auctionId, AuctionStatus.CANCELLED)
                .andExpect(status().isOk());

        // Ação: tenta sair do estado terminal
        changeAuctionStatus(token, auctionId, AuctionStatus.SCHEDULED)
                .andExpect(errorResponse(HttpStatus.CONFLICT))
                .andExpect(hasOnlyErrorResponseFields());
    }

    @Test
    @Transactional
    @DisplayName("Should return a 422 response with the error envelope when the bid is below the minimum allowed")
    void shouldReturnUnprocessableContentWithErrorEnvelopeWhenBidIsBelowMinimum() throws Exception {
        // Arranjo: lote aberto com valor atual 1000 e incremento mínimo 100
        Category category = categoryRepository.save(Categories.any());
        Auction auction = auctionRepository.save(Auctions.active());
        Lot lot = lotRepository.save(Lots.openFor(
                auction, category, new BigDecimal("1000.00"), new BigDecimal("100.00")));
        User user = userRepository.save(Users.any());

        String token = Tokens.signedByAppKey().build();
        BidRequest belowMinimum = new BidRequest( new BigDecimal("500.00"), user.getId());

        // Ação + verificação
        mockMvc.perform(withToken(post("/lots/{lotId}/bids", lot.getId()), token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(belowMinimum)))
                .andExpect(errorResponse(HttpStatus.UNPROCESSABLE_CONTENT))
                .andExpect(hasOnlyErrorResponseFields());
    }

    private static void assertIsoTimestamp(String json) {
        Object timestamp = JsonPath.read(json, "$.timestamp");

        assertThat(timestamp).as("timestamp deve ser texto").isInstanceOf(String.class);
        assertThatCode(() -> Instant.parse((String) timestamp)).doesNotThrowAnyException();
    }

    private String createAuction(String token) throws Exception {
        String body = mockMvc.perform(withToken(post("/auctions"), token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(AuctionRequests.valid())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        return JsonPath.read(body, "$.id");
    }

    private ResultActions changeAuctionStatus(String token, String auctionId, AuctionStatus newStatus) throws Exception {
        return mockMvc.perform(withToken(patch("/auctions/{id}/status", auctionId), token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new AuctionUpdateStatusRequest(newStatus))));
    }
}
