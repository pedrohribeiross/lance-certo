package br.com.leje.lancecerto.bid.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record BidRequest(
        @NotNull(message = "O lance é obrigatório")
        @Positive(message = "O lance deve ser maior que zero")
        BigDecimal value,

        @NotNull(message = "O licitante é obrigatório")
        UUID userId
) {
}
