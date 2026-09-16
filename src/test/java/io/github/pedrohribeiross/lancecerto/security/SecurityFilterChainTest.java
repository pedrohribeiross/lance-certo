package io.github.pedrohribeiross.lancecerto.security;

import io.github.pedrohribeiross.lancecerto.auction.dto.AuctionRequest;
import io.github.pedrohribeiross.lancecerto.support.IntegrationTest;
import io.github.pedrohribeiross.lancecerto.support.security.Tokens;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityFilterChainTest extends IntegrationTest {

    @Test
    @DisplayName("Should return a 401 response instead of a 500 response when the token is malformed")
    void shouldReturnUnauthorizedResponseWhenTokenIsMalformed() throws Exception {
        mockMvc.perform(withToken(post("/auctions"), "nao-e-um-jwt"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return a 401 response when the token is signed with another key")
    void shouldReturnUnauthorizedResponseWhenTokenIsSignedWithAnotherKey() throws Exception {
        String token = Tokens.signedByAnotherKey().build();

        mockMvc.perform(withToken(post("/auctions"), token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return a 401 response when the token is expired")
    void shouldReturnUnauthorizedResponseWhenTokenIsExpired() throws Exception {
        String token = Tokens.signedByAppKey().expired().build();

        mockMvc.perform(withToken(post("/auctions"), token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should maintain the previous behavior when a valid token is provided")
    void shouldMaintainPreviousBehaviorWhenValidTokenIsProvided() throws Exception {
        String token = Tokens.signedByAppKey().build();
        String body = toJson(makeAuctionRequest());

        mockMvc.perform(withToken(post("/auctions"), token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should expose the sub claim as the authenticated principal")
    void shouldExposeSubClaimAsAuthenticatedPrincipal() throws Exception {
        String expectedSub = UUID.randomUUID().toString();
        String token = Tokens.signedByAppKey().subject(expectedSub).build();

        mockMvc.perform(withToken(get("/auctions"), token))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername(expectedSub));
    }

    @Test
    @DisplayName("Should map the roles claim to an authority on the authenticated principal")
    void shouldMapRolesClaimToAuthorityOnAuthenticatedPrincipal() throws Exception {
        String token = Tokens.signedByAppKey().roles("ADMIN").build();

        mockMvc.perform(withToken(get("/auctions"), token))
                .andExpect(status().isOk())
                .andExpect(authenticated().withAuthentication(auth ->
                        assertThat(auth.getAuthorities())
                                .extracting(GrantedAuthority::getAuthority)
                                .contains("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Should return a 200 response when the public route is requested with a valid token")
    void shouldReturnOkResponseWhenPublicRouteIsRequestedWithValidToken() throws Exception {
        String token = Tokens.signedByAppKey().build();

        mockMvc.perform(withToken(get("/auctions"), token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return a 401 response when the Authorization header does not contain the Bearer scheme")
    void shouldReturnUnauthorizedResponseWhenAuthorizationHeaderDoesNotContainBearerScheme() throws Exception {
        String token = Tokens.signedByAppKey().build();

        mockMvc.perform(post("/auctions").header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }

    private AuctionRequest makeAuctionRequest() {
        Instant startDate = Instant.now().plus(7, ChronoUnit.DAYS);
        Instant endDate = Instant.now().plus(14, ChronoUnit.DAYS);

        return new AuctionRequest(
                "mock Title",
                "mock description",
                "mock principal",
                startDate,
                endDate
        );
    }
}
