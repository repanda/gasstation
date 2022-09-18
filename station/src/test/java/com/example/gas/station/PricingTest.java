package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasType;
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
}
