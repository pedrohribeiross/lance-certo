package io.github.pedrohribeiross.lancecerto.shared.error;

import io.github.pedrohribeiross.lancecerto.shared.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Escreve o envelope de erro direto numa HttpServletResponse.
 *
 * Por que isto existe: erros rejeitados na filter chain do Spring Security
 * nunca chegam ao @RestControllerAdvice. Este componente
 * permite que a camada de filtro produza o MESMO envelope que a camada MCV.
 */
@Component
@RequiredArgsConstructor
public class ErrorResponseWriter {

    private final ErrorResponseFactory factory;
    private final ObjectMapper objectMapper;

    public void write(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        ErrorResponse body = factory.build(status, message);

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
