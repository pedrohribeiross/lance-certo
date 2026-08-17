package br.com.leje.lancecerto.lot;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum LotStatus {
    AVAILABLE,
    AWARDED,
    SUSPENDED;

    private static final Map<LotStatus, Set<LotStatus>> TRANSITIONS = new EnumMap<>(LotStatus.class);

    static {
        TRANSITIONS.put(AVAILABLE, EnumSet.of(SUSPENDED));
        TRANSITIONS.put(SUSPENDED, EnumSet.of(AVAILABLE));
        TRANSITIONS.put(AWARDED, EnumSet.noneOf(LotStatus.class));
    }

    boolean canTransitionTo(LotStatus target) {
        return TRANSITIONS.getOrDefault(this, Set.of()).contains(target);
    }
}
