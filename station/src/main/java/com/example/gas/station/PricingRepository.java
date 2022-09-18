package com.example.gas.station;

import com.example.gas.station.pricing.Pricing;
import net.bigpoint.assessment.gasstation.GasType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PricingRepository {
    ConcurrentMap<GasType, Pricing> repository = new ConcurrentHashMap();

    public void addOrUpdate(Pricing pricing) {
        repository.putIfAbsent(pricing.type(), pricing);
    }

    public double findBy(GasType type) {
        Pricing pricing = repository.get(type);
        return pricing == null ? null : pricing.price();
    }
}
