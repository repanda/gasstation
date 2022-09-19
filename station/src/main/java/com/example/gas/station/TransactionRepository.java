package com.example.gas.station;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.example.gas.station.Transaction.Status.*;

public class TransactionRepository {
    private ConcurrentMap<String, Transaction> transactions = new ConcurrentHashMap();

    public void add(Transaction transaction) {
        transactions.put(UUID.randomUUID().toString(), transaction);
    }

    public int calculateNumberOfCancellationsTooExpensive() {
        return countByStatus(CANCELLED_TOO_EXPENSIVE);
    }

    public int calculateNumberOfCancellationsNoGas() {
        return countByStatus(CANCELLED_NO_GAS);
    }

    public int calculateNumberOfSales() {
        return countByStatus(SUCCESSFUL);
    }

    private int countByStatus(Transaction.Status status) {
        return (int) transactions.values()
                .stream()
                .filter(transaction -> status.equals(transaction.status()))
                .count();
    }
}
