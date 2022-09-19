package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PumpRepository {

    private ConcurrentMap<String, GasPump> pumps = new ConcurrentHashMap();

    public void add(Pump pump) {
        pumps.put(pump.getId(), pump.getGasPump());
    }

    public Collection<GasPump> all() {
        return Collections.unmodifiableCollection(pumps.values());
    }

    public Pump findByType(GasType type) {
        Map.Entry<String, GasPump> pumpEntry = pumps.entrySet().stream()
                .filter(entry -> entry.getValue().getGasType().equals(type))
                .findFirst().orElse(null);

        return new Pump(pumpEntry.getKey(), pumpEntry.getValue());
    }

    public void update(Pump pump) {
        pumps.putIfAbsent(pump.getId(), pump.getGasPump());
    }
}
