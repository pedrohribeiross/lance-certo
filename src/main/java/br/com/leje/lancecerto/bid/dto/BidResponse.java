package br.com.leje.lancecerto.bid.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record BidResponse(
        UUID id,
        UUID lotId,
        UUID userId,
        BigDecimal value,
        Instant createdAt
) {
}
