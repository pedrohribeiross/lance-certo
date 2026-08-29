package io.github.pedrohribeiross.lancecerto.lot.dto;

import io.github.pedrohribeiross.lancecerto.category.dto.CategoryResponse;
import io.github.pedrohribeiross.lancecerto.lot.LotStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record LotResponse(
        UUID id,
        UUID auctionId,
        String description,
        BigDecimal startingBid,
        BigDecimal currentValue,
        LotStatus status,
        CategoryResponse category
) {
}
