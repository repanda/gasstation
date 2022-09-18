package com.example.gas.station;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.example.gas.station.Transaction.Status.CANCELLED_NO_GAS;
import static com.example.gas.station.Transaction.Status.CANCELLED_TOO_EXPENSIVE;

public class TransactionRepository {
    private ConcurrentMap<String, Transaction> transactions = new ConcurrentHashMap();

    public void add(Transaction transaction) {
        transactions.put(UUID.randomUUID().toString(), transaction);
    }

    public int calculateNumberOfCancellationsTooExpensive() {
        return (int) transactions.values()
                .stream()
                .filter(transaction -> CANCELLED_TOO_EXPENSIVE.equals(transaction.status()))
                .count();
    }

    public int calculateNumberOfCancellationsNoGas() {
        return (int) transactions.values()
                .stream()
                .filter(transaction -> CANCELLED_NO_GAS.equals(transaction.status()))
                .count();
    }
}
