package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.example.gas.station.PricingTest.DIESEL_PRICE;
import static com.example.gas.station.PumpsTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReportingTest {

    final GasStationGateway gasStationGateway = new GasStationGateway();

    @Test
    public void calculate_number_of_cancellations_too_expensive() {
        gasStationGateway.addGasPump(new GasPump(GasType.DIESEL, INITIAL_GAS_CAPACITY));
        gasStationGateway.setPrice(GasType.DIESEL, DIESEL_PRICE);

        try {
            gasStationGateway.buyGas(GasType.DIESEL, 10, 3);
        } catch (Exception e) {
        }
        try {
            int clientMaxPricePerLiter = 2;
            gasStationGateway.buyGas(GasType.DIESEL, 10, clientMaxPricePerLiter);
        } catch (Exception e) {
        }

        int numberOfCancellationsTooExpensive = gasStationGateway.getNumberOfCancellationsTooExpensive();
        assertThat(numberOfCancellationsTooExpensive).isEqualTo(1);
    }

    @Test
    public void calculate_number_of_cancellations_no_gas() {
        gasStationGateway.addGasPump(new GasPump(GasType.DIESEL, INITIAL_GAS_CAPACITY));
        gasStationGateway.setPrice(GasType.DIESEL, DIESEL_PRICE);

        try {
            int bigQuantityOfGas = 100;
            gasStationGateway.buyGas(GasType.DIESEL, bigQuantityOfGas, 5);
        } catch (Exception e) {
        }

        int numberOfCancellationsNoGas = gasStationGateway.getNumberOfCancellationsNoGas();
        assertThat(numberOfCancellationsNoGas).isEqualTo(1);
    }

    @Test
    public void calculate_number_of_successful_sales() throws GasTooExpensiveException, NotEnoughGasException {
        gasStationGateway.addGasPump(new GasPump(GasType.DIESEL, INITIAL_GAS_CAPACITY));
        gasStationGateway.setPrice(GasType.DIESEL, DIESEL_PRICE);

        // try to buy more than gas in pump
        assertThatThrownBy(() -> gasStationGateway.buyGas(GasType.DIESEL, 100, 5))
                .isInstanceOf(NotEnoughGasException.class);

        new MultiRequestRunner().run(() -> gasStationGateway.buyGas(GasType.DIESEL, 20, 5));

        int numberOfSuccessfulSales = gasStationGateway.getNumberOfSales();
        assertThat(numberOfSuccessfulSales).isEqualTo(1);
    }

    @Test
    public void calculate_revenue() {
        gasStationGateway.addGasPump(new GasPump(GasType.DIESEL, 10));
        gasStationGateway.setPrice(GasType.DIESEL, DIESEL_PRICE);
        gasStationGateway.addGasPump(new GasPump(GasType.SUPER, 20));
        gasStationGateway.setPrice(GasType.SUPER, SUPER_PRICE);
        gasStationGateway.addGasPump(new GasPump(GasType.REGULAR, 15));
        gasStationGateway.setPrice(GasType.REGULAR, REGULAR_PRICE);


        new MultiRequestRunner().run(() -> gasStationGateway.buyGas(GasType.DIESEL, 20, 5),
                () -> gasStationGateway.buyGas(GasType.DIESEL, 10, 5),
                () -> gasStationGateway.buyGas(GasType.DIESEL, 10, 5), // cancelled

                () -> gasStationGateway.buyGas(GasType.SUPER, 10, 5),
                () -> gasStationGateway.buyGas(GasType.SUPER, 10, 5),

                () -> gasStationGateway.buyGas(GasType.REGULAR, 10, 5),
                () -> gasStationGateway.buyGas(GasType.REGULAR, 10, 5) // cancelled
        );

        double revenue = gasStationGateway.getRevenue();
        Assertions.assertThat(revenue).
                isEqualTo(82);
    }

}
