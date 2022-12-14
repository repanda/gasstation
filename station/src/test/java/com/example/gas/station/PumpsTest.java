package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static com.example.gas.station.PricingTest.DIESEL_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

public class PumpsTest {
    public static final double SUPER_PRICE = 2;
    public static final double REGULAR_PRICE = 2;

    Logger logger = LoggerFactory.getLogger(PumpsTest.class);

    public static final int INITIAL_GAS_CAPACITY = 30;
    final GasStationGateway gasStationGateway = new GasStationGateway();

    @Test
    public void listAllGasPumps() {
        GasPump pump = new GasPump(GasType.DIESEL, INITIAL_GAS_CAPACITY);
        gasStationGateway.addGasPump(pump);

        Collection<GasPump> pumps = gasStationGateway.getGasPumps();

        assertThat(pumps).hasSize(1);
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

    @Test
    public void simulateMultiCostumerBuyingGasAtTheSameTime() {
        gasStationGateway.addGasPump(new GasPump(GasType.DIESEL, 70));
        gasStationGateway.setPrice(GasType.DIESEL, DIESEL_PRICE);
        gasStationGateway.addGasPump(new GasPump(GasType.SUPER, 20));
        gasStationGateway.setPrice(GasType.SUPER, SUPER_PRICE);
        gasStationGateway.addGasPump(new GasPump(GasType.REGULAR, 40));
        gasStationGateway.setPrice(GasType.REGULAR, REGULAR_PRICE);

        new MultiRequestRunner().run(
                () -> gasStationGateway.buyGas(GasType.DIESEL, 20, 5),
                () -> gasStationGateway.buyGas(GasType.DIESEL, 20, 5),
                () -> gasStationGateway.buyGas(GasType.DIESEL, 20, 5),
                () -> gasStationGateway.buyGas(GasType.DIESEL, 20, 5),

                () -> gasStationGateway.buyGas(GasType.SUPER, 10, 5),
                () -> gasStationGateway.buyGas(GasType.SUPER, 10, 5),
                () -> gasStationGateway.buyGas(GasType.SUPER, 10, 5),

                () -> gasStationGateway.buyGas(GasType.REGULAR, 30, 5),
                () -> gasStationGateway.buyGas(GasType.REGULAR, 30, 5),
                () -> gasStationGateway.buyGas(GasType.REGULAR, 30, 5)
        );

        assertThat(gasStationGateway.getNumberOfSales()).isEqualTo(6);
        assertThat(gasStationGateway.getNumberOfCancellationsNoGas()).isEqualTo(4);
    }
}
