package io.github.pedrohribeiross.lancecerto.support.fixtures;

import io.github.pedrohribeiross.lancecerto.auction.Auction;
import io.github.pedrohribeiross.lancecerto.category.Category;
import io.github.pedrohribeiross.lancecerto.lot.Lot;
import io.github.pedrohribeiross.lancecerto.lot.LotStatus;

import java.math.BigDecimal;

public final class Lots {

    private Lots() {
    }

    public static Lot openFor(Auction auction, Category category, BigDecimal currentValue, BigDecimal minIncrement) {
        return new Lot(
                null,
                "mock lot description",
                currentValue,
                currentValue,
                minIncrement,
                LotStatus.AVAILABLE,
                category,
                auction
        );
    }
}
