package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;

import static com.example.gas.station.Transaction.Status.*;

public class PumpAggregate {

    private final TransactionRepository transactionRepository;

    public PumpAggregate(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    double bugGas(GasPump pump, GasType type, double amountInLiters, double maxPricePerLiter, double gasPrice) throws GasTooExpensiveException, NotEnoughGasException {
        if (gasPrice > maxPricePerLiter) {
            transactionRepository.add(new Transaction(CANCELLED_TOO_EXPENSIVE));
            throw new GasTooExpensiveException();
        }
        if (pump == null) {
            throw new RuntimeException("pump type not available! " + type);
        }
        if (pump.getRemainingAmount() < amountInLiters) {
            transactionRepository.add(new Transaction(CANCELLED_NO_GAS));
            throw new NotEnoughGasException();
        }

        transactionRepository.add(new Transaction(SUCCESSFUL));
        return amountInLiters * maxPricePerLiter;
    }

}
