package io.github.pedrohribeiross.lancecerto.shared.error;

import io.github.pedrohribeiross.lancecerto.shared.dto.ErrorResponse;
import io.github.pedrohribeiross.lancecerto.shared.dto.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class ErrorResponseFactory {

    public ErrorResponse build(HttpStatus status, String message) {
        return build(status, message, null);
    }

    public ErrorResponse build(
            HttpStatus status,
            String message,
            List<ValidationError> fieldErrors
    ) {
        return new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                fieldErrors
        );
    }
}
