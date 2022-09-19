package com.example.gas.station;

import com.example.gas.station.pricing.Pricing;
import com.example.gas.station.pricing.PricingRepository;
import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasStation;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;

import java.util.Collection;

import static com.example.gas.station.Transaction.Status.*;

public class GasStationGateway implements GasStation {

    private final PumpRepository pumpRepository;
    private final PricingRepository pricingRepository;
    private final TransactionRepository transactionRepository;

    public GasStationGateway() {
        pumpRepository = new PumpRepository();
        pricingRepository = new PricingRepository();
        transactionRepository = new TransactionRepository();
    }

    @Override
    public void addGasPump(GasPump pump) {
        pumpRepository.add(pump);
    }

    @Override
    public Collection<GasPump> getGasPumps() {
        return pumpRepository.all();
    }

    @Override
    public double buyGas(GasType type, double amountInLiters, double maxPricePerLiter) throws NotEnoughGasException, GasTooExpensiveException {
        double gasPrice = pricingRepository.findBy(type);
        if (gasPrice > maxPricePerLiter) {
            transactionRepository.add(new Transaction(CANCELLED_TOO_EXPENSIVE));
            throw new GasTooExpensiveException();
        }
        GasPump pump = pumpRepository.findByType(type);
        if (pump == null) {
            throw new RuntimeException("pump type not available! " + type);
        }
        if (pump.getRemainingAmount() < amountInLiters) {
            transactionRepository.add(new Transaction(CANCELLED_NO_GAS));
            throw new NotEnoughGasException();
        }

        transactionRepository.add(new Transaction(SUCCESSFUL));
        return 0;
    }

    @Override
    public double getRevenue() {
        return 0;
    }

    @Override
    public int getNumberOfSales() {
        return transactionRepository.calculateNumberOfSales();
    }

    @Override
    public int getNumberOfCancellationsNoGas() {
        return transactionRepository.calculateNumberOfCancellationsNoGas();
    }

    @Override
    public int getNumberOfCancellationsTooExpensive() {
        return transactionRepository.calculateNumberOfCancellationsTooExpensive();
    }

    @Override
    public double getPrice(GasType type) {
        return pricingRepository.findBy(type);
    }

    @Override
    public void setPrice(GasType type, double price) {
        pricingRepository.addOrUpdate(new Pricing(type, price));
    }
}
