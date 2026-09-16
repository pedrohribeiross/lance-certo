package io.github.pedrohribeiross.lancecerto.security;

import io.github.pedrohribeiross.lancecerto.support.IntegrationTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProtectedRoutesInventoryTest extends IntegrationTest {

    private static final String ANY_UUID = UUID.randomUUID().toString();
    private static final String ANY_BODY = "{}";

    private record Route(HttpMethod method, String path, String body) {

        static Route get(String path) {
            return new Route(HttpMethod.GET, path, null);
        }

        static Route post(String path) {
            return new Route(HttpMethod.POST, path, ANY_BODY);
        }

        static Route put(String path) {
            return new Route(HttpMethod.PUT, path, ANY_BODY);
        }

        static Route patch(String path) {
            return new Route(HttpMethod.PATCH, path, ANY_BODY);
        }

        static Route delete(String path) {
            return new Route(HttpMethod.DELETE, path, null);
        }

        @Override
        public String toString() {
            return method + " " + path;
        }
    }

    static Stream<Route> publicCollectionRoutes() {
        return Stream.of(
                Route.get("/auctions"),
                Route.get("/lots")
        );
    }

    static Stream<Route> publicItemRoutes() {
        return Stream.of(
                Route.get("/auctions/" + ANY_UUID),
                Route.get("/lots/" + ANY_UUID)
        );
    }

    static Stream<Route> protectedRoutes() {
        return Stream.of(
                Route.post("/auctions"),
                Route.put("/auctions/" + ANY_UUID),
                Route.patch("/auctions/" + ANY_UUID + "/status"),
                Route.delete("/auctions/" + ANY_UUID),
                Route.post("/auctions/" + ANY_UUID + "/lots"),
                Route.put("/lots/" + ANY_UUID),
                Route.patch("/lots/" + ANY_UUID + "/status"),
                Route.delete("/lots/" + ANY_UUID),
                Route.post("/lots/" + ANY_UUID + "/bids")
        );
    }

    @ParameterizedTest(name = "{0} should return a 200 response when the public collection route is requested without a token")
    @MethodSource("publicCollectionRoutes")
    void shouldReturnOkResponseWhenPublicCollectionRouteIsRequestedWithoutAToken(Route route) throws Exception {
        mockMvc.perform(build(route))
                .andExpect(status().isOk());
    }

    @ParameterizedTest(name = "{0} should return a 404 response when the public item route is requested without a token")
    @MethodSource("publicItemRoutes")
    void shouldReturnItemNotFoundResponseWhenPublicItemRouteIsRequestedWithoutAToken(Route route) throws Exception {
        mockMvc.perform(build(route))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest(name = "{0} should return a 401 response when the protected route is requested without a token")
    @MethodSource("protectedRoutes")
    void shouldReturnUnauthorizedResponseWhenProtectedRouteIsRequestedWithoutAToken(Route route) throws Exception {
        mockMvc.perform(build(route))
                .andExpect(status().isUnauthorized());
    }

    private MockHttpServletRequestBuilder build(Route route) {
        var builder = MockMvcRequestBuilders.request(route.method, route.path);
        if (route.body != null) {
            builder.contentType(MediaType.APPLICATION_JSON)
                    .content(route.body);
        }
        return builder;
    }
}
