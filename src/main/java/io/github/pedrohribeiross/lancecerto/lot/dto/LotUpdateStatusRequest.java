package io.github.pedrohribeiross.lancecerto.lot.dto;

import io.github.pedrohribeiross.lancecerto.lot.LotStatus;
import jakarta.validation.constraints.NotNull;

public record LotUpdateStatusRequest(
        @NotNull(message = "O status é obrigatório")
        LotStatus status
) {
}
