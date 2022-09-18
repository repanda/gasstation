package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static com.example.gas.station.PricingTest.DIESEL_PRICE;

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

    @Test
    public void should_not_buy_gas_when_client_request_is_higher_than_station_tank() {
        gasStationGateway.addGasPump(new GasPump(GasType.DIESEL, INITIAL_GAS_CAPACITY));
        gasStationGateway.setPrice(GasType.DIESEL, DIESEL_PRICE);

        org.junit.jupiter.api.Assertions.assertThrows(
                NotEnoughGasException.class,
                () -> gasStationGateway.buyGas(GasType.DIESEL, 31, 2.2)
        );
    }
}
