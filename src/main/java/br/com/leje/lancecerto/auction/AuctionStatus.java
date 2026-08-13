package br.com.leje.lancecerto.auction;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum AuctionStatus {
    SCHEDULED,
    ACTIVE,
    CLOSED,
    CANCELLED;

    private static final Map<AuctionStatus, Set<AuctionStatus>> TRANSITIONS = new EnumMap<>(AuctionStatus.class);

    static {
        TRANSITIONS.put(SCHEDULED, EnumSet.of(ACTIVE, CANCELLED));
        TRANSITIONS.put(ACTIVE, EnumSet.of(CLOSED));
        TRANSITIONS.put(CLOSED, EnumSet.noneOf(AuctionStatus.class));
        TRANSITIONS.put(CANCELLED, EnumSet.noneOf(AuctionStatus.class));
    }

    public boolean canTransitionTo(AuctionStatus target) {
        return TRANSITIONS.getOrDefault(this, Set.of()).contains(target);
    }
}
