package io.github.pedrohribeiross.lancecerto.support.fixtures;

import io.github.pedrohribeiross.lancecerto.auction.Auction;
import io.github.pedrohribeiross.lancecerto.auction.AuctionStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class Auctions {

    private Auctions() {
    }

    public static Auction active() {
        Instant now = Instant.now();

        return new Auction(
                null,
                "mock Title",
                "mock description",
                "mock principal",
                now.minus(1, ChronoUnit.HOURS),
                now.plus(7, ChronoUnit.DAYS),
                AuctionStatus.ACTIVE
        );
    }
}
