package io.github.pedrohribeiross.lancecerto.shared.dto;

public record ValidationError(
        String field,
        String message
) {
}
