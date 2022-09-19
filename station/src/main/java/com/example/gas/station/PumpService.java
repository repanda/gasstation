package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.example.gas.station.Transaction.Status.*;

public class PumpService {

    private Logger logger = LoggerFactory.getLogger(PumpService.class);

    private final TransactionRepository transactionRepository;
    private final PumpRepository pumpRepository;

    public PumpService(TransactionRepository transactionRepository, PumpRepository pumpRepository) {
        this.transactionRepository = transactionRepository;
        this.pumpRepository = pumpRepository;
    }

    CustomerRequest buyGas(GasType type, double amountInLiters, double maxPricePerLiter, double gasPrice) throws GasTooExpensiveException, NotEnoughGasException {
        if (gasPrice > maxPricePerLiter) {
            transactionRepository.add(new Transaction(CANCELLED_TOO_EXPENSIVE));
            throw new GasTooExpensiveException();
        }
        Pump pump = pumpRepository.findByType(type);
        if (pump == null) {
            throw new RuntimeException("pump not available! " + type);
        }
        double remainingAmount = pump.getGasPump().getRemainingAmount();
        logger.info("remaining gas amount before pumping:" + remainingAmount);
        if (remainingAmount < amountInLiters) {
            transactionRepository.add(new Transaction(CANCELLED_NO_GAS));
            throw new NotEnoughGasException();
        }

        return new CustomerRequest(amountInLiters, gasPrice);
    }

    public void applyCostumerTransaction(CustomerRequest request) {
        logger.info("request = " + request);

        Pump pump = pumpRepository.findByType(GasType.DIESEL);
        double remainingAmount = pump.getGasPump().getRemainingAmount();
        logger.info("remaining gas amount before pumping:" + remainingAmount);
        if (remainingAmount < request.amountInLiters()) {
            transactionRepository.add(new Transaction(CANCELLED_NO_GAS));
            return;
        }

        pump.getGasPump().pumpGas(request.amountInLiters());
        logger.info("remaining gas amount after pumping:" + pump.getGasPump().getRemainingAmount());
        pumpRepository.update(pump);
        transactionRepository.add(new Transaction(SUCCESSFUL));
    }
}