package io.github.pedrohribeiross.lancecerto.auction.dto;

import io.github.pedrohribeiross.lancecerto.auction.AuctionStatus;
import jakarta.validation.constraints.NotNull;

public record AuctionUpdateStatusRequest(
        @NotNull(message = "O status é obrigatório")
        AuctionStatus status
) {
}
