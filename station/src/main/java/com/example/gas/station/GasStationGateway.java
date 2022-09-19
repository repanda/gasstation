package com.example.gas.station;

import com.example.gas.station.pricing.Pricing;
import com.example.gas.station.pricing.PricingRepository;
import com.example.gas.station.util.CostumerRequestStore;
import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasStation;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;

import java.util.Collection;

public class GasStationGateway implements GasStation {

    private final PumpRepository pumpRepository;
    private final PricingRepository pricingRepository;
    private final TransactionRepository transactionRepository;
    private final CostumerRequestStore costumerRequestStore;
    private final PumpService pumpService;

    public GasStationGateway() {
        pumpRepository = new PumpRepository();
        pricingRepository = new PricingRepository();
        transactionRepository = new TransactionRepository();
        pumpService = new PumpService(transactionRepository, pumpRepository);
        costumerRequestStore = new CostumerRequestStore(pumpService);
    }

    @Override
    public void addGasPump(GasPump gasPump) {
        pumpRepository.add(new Pump(gasPump));
    }

    @Override
    public Collection<GasPump> getGasPumps() {
        return pumpRepository.all();
    }

    @Override
    public double buyGas(GasType type, double amountInLiters, double maxPricePerLiter) throws NotEnoughGasException, GasTooExpensiveException {
        double gasPrice = pricingRepository.findBy(type);
        CustomerRequest request = pumpService.buyGas(type, amountInLiters, maxPricePerLiter, gasPrice);
        costumerRequestStore.publishRequest(request);
        return request.amountInLiters() * request.gasPrice();
    }

    @Override
    public double getRevenue() {
        return transactionRepository.calculateRevenue();
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
