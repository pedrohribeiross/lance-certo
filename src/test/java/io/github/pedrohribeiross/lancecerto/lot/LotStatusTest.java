package io.github.pedrohribeiross.lancecerto.lot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LotStatusTest {

    @Test
    @DisplayName("Should transition from AVAILABLE to SUSPENDED")
    void availableToSuspendedShouldBeAllowed() {
        assertThat(LotStatus.AVAILABLE.canTransitionTo(LotStatus.SUSPENDED)).isTrue();
    }

    @Test
    @DisplayName("Should transition from SUSPENDED to AVAILABLE")
    void suspendedToAvailableShouldBeAllowed() {
        assertThat(LotStatus.SUSPENDED.canTransitionTo(LotStatus.AVAILABLE)).isTrue();
    }

    @Test
    @DisplayName("Should not transition from any status to AWARDED")
    void anyStatusToAwardedShouldNotBeAllowed() {
        assertThat(LotStatus.SUSPENDED.canTransitionTo(LotStatus.AWARDED)).isFalse();
    }

    @Test
    @DisplayName("Should not transition from AVAILABLE to AWARDED")
    void availableToAwardedShouldNotBeAllowed() {
        assertThat(LotStatus.AVAILABLE.canTransitionTo(LotStatus.AWARDED)).isFalse();
    }

    @Test
    @DisplayName("Should not transition from AWARDED to any other status")
    void anyOtherStatusShouldNotBeAllowed() {
        assertThat(LotStatus.AWARDED.canTransitionTo(LotStatus.AVAILABLE)).isFalse();
    }

    @Test
    @DisplayName("Should not transition to the same status")
    void sameStatusShouldNotBeAllowed() {
        assertThat(LotStatus.AVAILABLE.canTransitionTo(LotStatus.AVAILABLE)).isFalse();
    }
}