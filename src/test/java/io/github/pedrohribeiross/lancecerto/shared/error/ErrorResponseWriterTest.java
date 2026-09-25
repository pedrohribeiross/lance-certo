package io.github.pedrohribeiross.lancecerto.shared.error;

import io.github.pedrohribeiross.lancecerto.support.web.ErrorResponseMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@JsonTest
class ErrorResponseWriterTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Should write the error envelope with an ISO-8601 timestamp using the Spring-configured ObjectMapper")
    void shouldWriteErrorEnvelopeWithIsoTimestampUsingSpringObjectMapper() throws Exception {
        var writer = new ErrorResponseWriter(new ErrorResponseFactory(), objectMapper);
        var response = new MockHttpServletResponse();

        writer.write(response, HttpStatus.UNAUTHORIZED, "any message");

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentType()).startsWith(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getCharacterEncoding()).isEqualToIgnoringCase("UTF-8");

        Map<String, Object> body = objectMapper.readValue(
                response.getContentAsString(StandardCharsets.UTF_8),
                new TypeReference<>() {
                }
        );

        assertThat(body.keySet())
                .containsExactlyInAnyOrderElementsOf(ErrorResponseMatchers.ERROR_RESPONSE_FIELDS);
        assertThat(body)
                .containsEntry("status", HttpStatus.UNAUTHORIZED.value())
                .containsEntry("error", "Unauthorized")
                .containsEntry("message", "any message");

        assertThat(body.get("timestamp")).isInstanceOf(String.class);
        assertThatCode(() -> Instant.parse((String) body.get("timestamp"))).doesNotThrowAnyException();
    }
}