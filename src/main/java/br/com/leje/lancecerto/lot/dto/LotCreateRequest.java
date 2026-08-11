package br.com.leje.lancecerto.lot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record LotCreateRequest(
        @NotBlank(message = "A descrição é obrigatória")
        String description,

        @NotNull(message = "A categoria é obrigatória")
        UUID categoryId,

        @NotNull(message = "O lance inicial é obrigatório")
        @Positive(message = "O lance inicial deve ser maior que zero")
        BigDecimal startingBid
) {
}
