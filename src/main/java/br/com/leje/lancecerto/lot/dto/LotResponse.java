package br.com.leje.lancecerto.lot.dto;

import br.com.leje.lancecerto.category.dto.CategoryResponse;
import br.com.leje.lancecerto.lot.LotStatus;

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
