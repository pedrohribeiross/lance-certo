package io.github.pedrohribeiross.lancecerto.auction;

import io.github.pedrohribeiross.lancecerto.shared.exception.InvalidStatusTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuctionTest {

    private Auction auction;

    @BeforeEach
    void setUp() {
        auction = new Auction();
    }

    @Test
    @DisplayName("Should have an initial status of SCHEDULED")
    void shouldHaveInitialStatusOfSCHEDULED() {
        assertThat(auction.getStatus()).isEqualTo(AuctionStatus.SCHEDULED);
    }

    @Nested
    class transitionTo {

        @Test
        @DisplayName("Should transition to a valid status")
        void transitionToValid() {
            auction.transitionTo(AuctionStatus.ACTIVE);

            assertThat(auction.getStatus()).isEqualTo(AuctionStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should transition from SCHEDULED to CANCELLED")
        void transitionFromSCHEDULEDToCANCELLED() {
            auction.transitionTo(AuctionStatus.CANCELLED);

            assertThat(auction.getStatus()).isEqualTo(AuctionStatus.CANCELLED);
        }

        @Test
        @DisplayName("Should throw an error when transitioning to an invalid status")
        void transitionToInvalid() {
            assertThatThrownBy(() -> auction.transitionTo(AuctionStatus.CLOSED)).isInstanceOf(InvalidStatusTransitionException.class);
        }

        @Test
        @DisplayName("Should throw an error when transitioning to the same status")
        void transitionToSameStatus() {
            assertThat(auction.getStatus()).isEqualTo(AuctionStatus.SCHEDULED);
            assertThatThrownBy(() -> auction.transitionTo(AuctionStatus.SCHEDULED)).isInstanceOf(InvalidStatusTransitionException.class);
        }
    }
}