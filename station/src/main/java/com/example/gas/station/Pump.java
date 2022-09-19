package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasPump;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Wrapper for GasPump object
 */
public class Pump {

    private String id;
    private GasPump gasPump;

    public Pump(GasPump gasPump) {
        requireNonNull(gasPump);
        this.gasPump = gasPump;
        this.id = UUID.randomUUID().toString();
    }

    public Pump(String id, GasPump gasPump) {
        this.id = id;
        this.gasPump = gasPump;
    }

    public GasPump getGasPump() {
        return gasPump;
    }

    public String getId() {
        return id;
    }
}
