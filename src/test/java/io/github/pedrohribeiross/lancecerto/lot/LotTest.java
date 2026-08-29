package io.github.pedrohribeiross.lancecerto.lot;

import io.github.pedrohribeiross.lancecerto.shared.exception.BidBelowMinIncrementException;
import io.github.pedrohribeiross.lancecerto.shared.exception.BidTooLowException;
import io.github.pedrohribeiross.lancecerto.shared.exception.InvalidStatusTransitionException;
import io.github.pedrohribeiross.lancecerto.shared.exception.LotNotAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LotTest {

    private Lot lot;

    @BeforeEach
    void setUp() {
        lot = new Lot();
    }

    @Test
    @DisplayName("Should have an initial status of AVAILABLE")
    void availableStatusShouldHaveInitialStatus() {
        assertThat(lot.getStatus()).isEqualTo(LotStatus.AVAILABLE);
    }

    @Nested
    class transitionTo {
        @Test
        @DisplayName("Should transition to a valid status")
        void transitionToValidStatus() {
            lot.transitionTo(LotStatus.SUSPENDED);
            assertThat(lot.getStatus()).isEqualTo(LotStatus.SUSPENDED);
        }

        @Test
        @DisplayName("Should throw an error when transitioning to AWARDED")
        void transitionToAwardedStatusThrowsAnError() {
            assertThatThrownBy(() -> lot.transitionTo(LotStatus.AWARDED)).isInstanceOf(InvalidStatusTransitionException.class);
        }

        @Test
        @DisplayName("Should throw an error when transitioning to same status")
        void transitionToSameStatusThrowsAnError() {
            assertThat(lot.getStatus()).isEqualTo(LotStatus.AVAILABLE);
            assertThatThrownBy(() -> lot.transitionTo(LotStatus.AVAILABLE)).isInstanceOf(InvalidStatusTransitionException.class);
        }
    }

    @Nested
    class placeBid {

        @BeforeEach
        void setUp() {
            lot = new Lot();
            lot.setStartingBid(BigDecimal.valueOf(1000));
            lot.setCurrentValue(BigDecimal.valueOf(1000));
            lot.setMinIncrement(BigDecimal.valueOf(10));
        }

        @Test
        @DisplayName("Should place a bid when the amount is valid")
        void shouldPlaceBidWhenAmountIsValid() {
            lot.placeBid(BigDecimal.valueOf(12000));

            assertThat(lot.getCurrentValue()).isEqualByComparingTo(BigDecimal.valueOf(12000));
        }

        @Test
        @DisplayName("Should not place a bid when the lot is not available")
        void shouldNotPlaceBidWhenLotIsNotAvailable() {
            lot.transitionTo(LotStatus.SUSPENDED);
            assertThatThrownBy(() -> lot.placeBid(BigDecimal.valueOf(2000))).isInstanceOf(LotNotAvailableException.class);
        }

        @Test
        @DisplayName("Should not place a bid when the amount is less than or equal to the current value")
        void shouldNotPlaceBidWhenAmountIsLessThanOrEqualToCurrentValue() {
            assertThatThrownBy(() -> lot.placeBid(BigDecimal.valueOf(500))).isInstanceOf(BidTooLowException.class);
        }

        @Test
        @DisplayName("Should not place a bid when the amount is lower than the current value plus the minimum increment")
        void shouldNotPlaceBidWhenAmountIsLowerThanCurrentValuePlusMinimumIncrement() {
            assertThatThrownBy(() -> lot.placeBid(BigDecimal.valueOf(1009.99))).isInstanceOf(BidBelowMinIncrementException.class);
        }
    }
}