package com.example.gas.station;

public record Transaction(Status status) {
    enum Status {
        CANCELLED_TOO_EXPENSIVE
    }
}
