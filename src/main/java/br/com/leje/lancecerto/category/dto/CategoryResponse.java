package br.com.leje.lancecerto.category.dto;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name
) {
}
