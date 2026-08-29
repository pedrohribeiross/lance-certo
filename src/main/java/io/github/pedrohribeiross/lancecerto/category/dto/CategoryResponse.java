package io.github.pedrohribeiross.lancecerto.category.dto;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name
) {
}
