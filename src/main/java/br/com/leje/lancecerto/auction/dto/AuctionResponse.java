package br.com.leje.lancecerto.auction.dto;

import br.com.leje.lancecerto.auction.AuctionStatus;

import java.time.Instant;
import java.util.UUID;

public record AuctionResponse(
        UUID id,
        String title,
        String description,
        String principal,
        Instant startDate,
        Instant endDate,
        AuctionStatus status
) {
}
