package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasType;

public record CustomerRequest(GasType gasType, double amountInLiters, double gasPrice) {
}
