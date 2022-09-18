package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PricingTest {

    public static final double DIESEL_PRICE = 2.2;
    final GasStationGateway gasStationGateway = new GasStationGateway();

    @Test
    public void priceTest() {
        gasStationGateway.setPrice(GasType.DIESEL, DIESEL_PRICE);

        double price = gasStationGateway.getPrice(GasType.DIESEL);

        Assertions.assertThat(price).isEqualTo(DIESEL_PRICE);
    }

    @Test
    public void should_not_buy_gas_when_client_price_under_gas_type_price() {
        gasStationGateway.setPrice(GasType.DIESEL, DIESEL_PRICE);

        org.junit.jupiter.api.Assertions.assertThrows(
                GasTooExpensiveException.class,
                () -> gasStationGateway.buyGas(GasType.DIESEL, 10, 2)
        );
    }


}
