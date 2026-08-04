package br.com.leje.lancecerto.auction.dto;

import br.com.leje.lancecerto.auction.AuctionStatus;
import jakarta.validation.constraints.NotNull;

public record AuctionUpdateStatusRequest(
        @NotNull(message = "O status é obrigatório")
        AuctionStatus status
) {
}
