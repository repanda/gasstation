package com.example.gas.station;

import com.example.gas.station.pricing.Pricing;
import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasStation;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;

import java.util.Collection;

public class GasStationGateway implements GasStation {

    private final PumpRepository pumpRepository;
    private final PricingRepository pricingRepository;

    public GasStationGateway() {
        pumpRepository = new PumpRepository();
        pricingRepository = new PricingRepository();
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
        return 0;
    }

    @Override
    public double getRevenue() {
        return 0;
    }

    @Override
    public int getNumberOfSales() {
        return 0;
    }

    @Override
    public int getNumberOfCancellationsNoGas() {
        return 0;
    }

    @Override
    public int getNumberOfCancellationsTooExpensive() {
        return 0;
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
