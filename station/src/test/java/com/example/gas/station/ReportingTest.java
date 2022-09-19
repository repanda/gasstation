package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.example.gas.station.PricingTest.DIESEL_PRICE;
import static com.example.gas.station.PumpsTest.INITIAL_GAS_CAPACITY;

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
        Assertions.assertThat(numberOfCancellationsTooExpensive).isEqualTo(1);
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
        Assertions.assertThat(numberOfCancellationsNoGas).isEqualTo(1);
    }

    @Test
    public void calculate_number_of_successful_sales() throws GasTooExpensiveException, NotEnoughGasException {
        gasStationGateway.addGasPump(new GasPump(GasType.DIESEL, INITIAL_GAS_CAPACITY));
        gasStationGateway.setPrice(GasType.DIESEL, DIESEL_PRICE);

        try {
            int bigQuantityOfGas = 100;
            gasStationGateway.buyGas(GasType.DIESEL, bigQuantityOfGas, 5);
        } catch (Exception e) {
        }

        gasStationGateway.buyGas(GasType.DIESEL, 20, 5);

        int numberOfSuccessfulSales = gasStationGateway.getNumberOfSales();
        Assertions.assertThat(numberOfSuccessfulSales).isEqualTo(1);
    }
}
