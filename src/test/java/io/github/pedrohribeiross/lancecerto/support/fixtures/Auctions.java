package io.github.pedrohribeiross.lancecerto.support.fixtures;

import io.github.pedrohribeiross.lancecerto.auction.Auction;
import io.github.pedrohribeiross.lancecerto.auction.AuctionStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class Auctions {

    private Auctions() {
    }

    public static Auction with(String title, AuctionStatus status) {
        Instant now = Instant.now();

        return new Auction(
                null,
                title,
                "mock description",
                "mock principal",
                now.minus(1, ChronoUnit.HOURS),
                now.plus(7, ChronoUnit.DAYS),
                status
        );
    }

    public static Auction active() {
        return with("mock Title", AuctionStatus.ACTIVE);
    }

    public static Auction scheduled() {
        return with("mock Title", AuctionStatus.SCHEDULED);
    }
}
