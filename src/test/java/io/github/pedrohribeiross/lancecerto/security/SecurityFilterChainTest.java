package io.github.pedrohribeiross.lancecerto.security;

import io.github.pedrohribeiross.lancecerto.support.IntegrationTest;
import io.github.pedrohribeiross.lancecerto.support.fixtures.AuctionRequests;
import io.github.pedrohribeiross.lancecerto.support.security.Tokens;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

import static io.github.pedrohribeiross.lancecerto.support.web.ErrorResponseMatchers.unauthorizedErrorResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityFilterChainTest extends IntegrationTest {

    @Test
    @DisplayName("Should return a 401 response in the error envelope instead of a 500 response when the token is malformed")
    void shouldReturnUnauthorizedResponseWhenTokenIsMalformed() throws Exception {
        mockMvc.perform(withToken(post("/auctions"), "nao-e-um-jwt"))
                .andExpect(unauthorizedErrorResponse());
    }

    @Test
    @DisplayName("Should return a 401 response in the error envelope when the token is signed with another key")
    void shouldReturnUnauthorizedResponseWhenTokenIsSignedWithAnotherKey() throws Exception {
        String token = Tokens.signedByAnotherKey().build();

        mockMvc.perform(withToken(post("/auctions"), token))
                .andExpect(unauthorizedErrorResponse());
    }

    @Test
    @DisplayName("Should return a 401 response in the error envelope when the token is expired")
    void shouldReturnUnauthorizedResponseWhenTokenIsExpired() throws Exception {
        String token = Tokens.signedByAppKey().expired().build();

        mockMvc.perform(withToken(post("/auctions"), token))
                .andExpect(unauthorizedErrorResponse());
    }

    @Test
    @DisplayName("Should maintain the previous behavior when a valid token is provided")
    void shouldMaintainPreviousBehaviorWhenValidTokenIsProvided() throws Exception {
        String token = Tokens.signedByAppKey().build();
        String body = toJson(AuctionRequests.valid());

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
    @DisplayName("Should return a 401 response in the error envelope when the Authorization header does not contain the Bearer scheme")
    void shouldReturnUnauthorizedResponseWhenAuthorizationHeaderDoesNotContainBearerScheme() throws Exception {
        String token = Tokens.signedByAppKey().build();

        mockMvc.perform(post("/auctions").header("Authorization", token))
                .andExpect(unauthorizedErrorResponse());
    }

    @Test
    @DisplayName("Should emit the WWW-Authenticate header with the Bearer scheme on a 401 response")
    void shouldEmitWwwAuthenticateHeaderOnUnauthorizedResponse() throws Exception {
        mockMvc.perform(post("/auctions"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string(HttpHeaders.WWW_AUTHENTICATE, startsWith("Bearer")));
    }

    @Test
    @DisplayName("Should return a 401 response in the error envelope when a public route is requested with a token signed by another key")
    void shouldReturnUnauthorizedEnvelopeWhenPublicRouteIsRequestedWithTokenSignedByAnotherKey() throws Exception {
        String token = Tokens.signedByAnotherKey().build();

        mockMvc.perform(withToken(get("/auctions"), token))
                .andExpect(unauthorizedErrorResponse());
    }
}
