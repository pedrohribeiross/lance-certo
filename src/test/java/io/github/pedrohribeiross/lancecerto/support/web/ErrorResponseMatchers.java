package io.github.pedrohribeiross.lancecerto.support.web;

import com.jayway.jsonpath.JsonPath;
import io.github.pedrohribeiross.lancecerto.security.AuthErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public final class ErrorResponseMatchers {

    public static final Set<String> ERROR_RESPONSE_FIELDS = Set.of("timestamp", "status", "error", "message");

    private ErrorResponseMatchers() {
    }

    public static ResultMatcher errorResponse(HttpStatus status) {
        return result -> {
            status().is(status.value()).match(result);
            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON).match(result);
            jsonPath("$.timestamp").isString().match(result);
            jsonPath("$.status").value(status.value()).match(result);
            jsonPath("$.error").value(status.getReasonPhrase()).match(result);
            jsonPath("$.message").isNotEmpty().match(result);
        };
    }

    public static ResultMatcher unauthorizedErrorResponse() {
        return result -> {
            errorResponse(HttpStatus.UNAUTHORIZED).match(result);
            jsonPath("$.message").value(AuthErrorMessages.AUTHENTICATION_REQUIRED).match(result);
            jsonPath("$.fieldErrors").doesNotHaveJsonPath().match(result);
        };
    }

    public static ResultMatcher hasOnlyErrorResponseFields() {
        return result -> {
            String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
            Map<String, Object> body = JsonPath.read(json, "$");

            assertThat(body.keySet())
                    .as("chaves do corpo de erro")
                    .containsExactlyInAnyOrderElementsOf(ERROR_RESPONSE_FIELDS);
        };
    }
}
