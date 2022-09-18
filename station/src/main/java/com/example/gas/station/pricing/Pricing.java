package com.example.gas.station.pricing;

import net.bigpoint.assessment.gasstation.GasType;

public record Pricing(GasType type, double price) {
}
