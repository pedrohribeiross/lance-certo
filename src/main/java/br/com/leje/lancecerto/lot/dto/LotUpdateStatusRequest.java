package br.com.leje.lancecerto.lot.dto;

import br.com.leje.lancecerto.lot.LotStatus;
import jakarta.validation.constraints.NotNull;

public record LotUpdateStatusRequest(
        @NotNull(message = "O status é obrigatório")
        LotStatus status
) {
}
