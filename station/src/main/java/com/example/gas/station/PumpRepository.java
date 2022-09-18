package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasPump;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PumpRepository {

    ConcurrentMap<String, GasPump> pumps = new ConcurrentHashMap();

    public void add(GasPump pump) {
        pumps.put(UUID.randomUUID().toString(), pump);
    }

    public Collection<GasPump> all() {
        return Collections.unmodifiableCollection(pumps.values());
    }
}
