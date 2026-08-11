package br.com.leje.lancecerto.lot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LotUpdateRequest(
        @NotBlank(message = "A descrição é obrigatória")
        String description,

        @NotNull(message = "A categoria é obrigatória")
        UUID categoryId
) {
}
