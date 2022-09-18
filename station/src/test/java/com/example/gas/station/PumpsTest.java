package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class PumpsTest {

    public static final int INITIAL_GAS_CAPACITY = 30;
    final GasStationGateway gasStationGateway = new GasStationGateway();

    @Test
    public void listAllGasPumps() {
        GasPump pump = new GasPump(GasType.DIESEL, INITIAL_GAS_CAPACITY);
        gasStationGateway.addGasPump(pump);

        Collection<GasPump> pumps = gasStationGateway.getGasPumps();

        Assertions.assertThat(pumps).hasSize(1);
    }
}
