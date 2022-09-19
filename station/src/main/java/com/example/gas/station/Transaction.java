package com.example.gas.station;

public record Transaction(Status status, double totalAmount) {
    enum Status {
        CANCELLED_TOO_EXPENSIVE,
        CANCELLED_NO_GAS,
        SUCCESSFUL
    }
}
