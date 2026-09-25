package io.github.pedrohribeiross.lancecerto.support.fixtures;

import io.github.pedrohribeiross.lancecerto.auction.dto.AuctionRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class AuctionRequests {

    private AuctionRequests() {}

    public static AuctionRequest valid(){
        return withTitle("mock title");
    }

    public static AuctionRequest withTitle(String title) {
        Instant startDate = Instant.now().plus(7, ChronoUnit.DAYS);
        Instant endDate = Instant.now().plus(14, ChronoUnit.DAYS);

        return new AuctionRequest(
                title,
                "mock description",
                "mock principal",
                startDate,
                endDate
        );
    }
}
