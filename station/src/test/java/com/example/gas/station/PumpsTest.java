package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class PumpsTest {


    @Test
    public void listAllGasPumps() {
        GasStationGateway gasStationGateway = new GasStationGateway();

        GasPump pump = new GasPump(GasType.DIESEL, 2.2);
        gasStationGateway.addGasPump(pump);

        Collection<GasPump> pumps = gasStationGateway.getGasPumps();
        Assertions.assertThat(pumps).hasSize(1);
    }
}
