package br.com.leje.lancecerto.lot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LotStatusTest {

    // alterar de AVAILABLE para SUSPENDED é true
    @Test
    @DisplayName("Should transition from AVAILABLE to SUSPENDED")
    void availableToSuspendedShouldBeAllowed(){
        assertThat(LotStatus.AVAILABLE.canTransitionTo(LotStatus.SUSPENDED)).isTrue();
    }

    // alterar de SUSPENDED para AVAILABLE é true
    @Test
    @DisplayName("Should transition from SUSPENDED to AVAILABLE")
    void suspendedToAvailableShouldBeAllowed(){
        assertThat(LotStatus.SUSPENDED.canTransitionTo(LotStatus.AVAILABLE)).isTrue();
    }
    // qualquer acesso até AWARED é false
    // alterar AWARED para qualquer status é false
    // mesmo status é false
}