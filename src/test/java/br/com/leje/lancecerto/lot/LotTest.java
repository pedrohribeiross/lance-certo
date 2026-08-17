package br.com.leje.lancecerto.lot;

import br.com.leje.lancecerto.shared.exception.InvalidStatusTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
}