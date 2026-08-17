package br.com.leje.lancecerto.auction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuctionStatusTest {

    @Test
    @DisplayName("Should transition to a valid status")
    void shouldTransitionToAValidStatus() {
        assertThat(AuctionStatus.SCHEDULED.canTransitionTo(AuctionStatus.ACTIVE)).isTrue();
    }

    @Test
    @DisplayName("Should not transition to an invalid status")
    void shouldNotTransitionToAnInvalidStatus() {
        assertThat(AuctionStatus.ACTIVE.canTransitionTo(AuctionStatus.SCHEDULED)).isFalse();
    }

    @Test
    @DisplayName("Should not transition to the same status")
    void shouldNotTransitionToTheSameStatus() {
        assertThat(AuctionStatus.SCHEDULED.canTransitionTo(AuctionStatus.SCHEDULED)).isFalse();
    }

    @Test
    @DisplayName("Should not transition another status from a terminal status")
    void shouldNotTransitionFromTerminalStatus() {
        assertThat(AuctionStatus.CLOSED.canTransitionTo(AuctionStatus.SCHEDULED)).isFalse();
        assertThat(AuctionStatus.CANCELLED.canTransitionTo(AuctionStatus.ACTIVE)).isFalse();
    }

    @Test
    @DisplayName("Should not transition from ACTIVE to CANCELLED")
    void shouldNotTransitionFromActiveStatus() {
        assertThat(AuctionStatus.ACTIVE.canTransitionTo(AuctionStatus.CANCELLED)).isFalse();
    }
}


