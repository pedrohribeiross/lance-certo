package io.github.pedrohribeiross.lancecerto.lot.dto;

import io.github.pedrohribeiross.lancecerto.lot.LotStatus;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record LotFilterRequest(
        UUID category,
        LotStatus status,

        @Positive(message = "O valor mínimo deve ser maior que zero")
        BigDecimal minValue,

        @Positive(message = "O valor máximo deve ser maior que zero")
        BigDecimal maxValue,
        String search
) {
}
