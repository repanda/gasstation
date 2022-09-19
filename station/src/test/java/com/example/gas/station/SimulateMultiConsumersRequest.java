package com.example.gas.station;

import net.bigpoint.assessment.gasstation.GasType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulateMultiConsumersRequest {
    Logger logger = LoggerFactory.getLogger(SimulateMultiConsumersRequest.class);

    public SimulateMultiConsumersRequest(GasStationGateway gasStationGateway) {
        ExecutorService service = Executors.newFixedThreadPool(4);

        List<Callable<Double>> futureList = new ArrayList<>();
        futureList.add(() -> gasStationGateway.buyGas(GasType.DIESEL, 20, 5));
        futureList.add(() -> gasStationGateway.buyGas(GasType.DIESEL, 20, 5));
        futureList.add(() -> gasStationGateway.buyGas(GasType.DIESEL, 20, 5));
        futureList.add(() -> gasStationGateway.buyGas(GasType.DIESEL, 20, 5));

        logger.info("Start 4 threads at the same time");
        try {
            service.invokeAll(futureList);
        } catch (Exception err) {
            err.printStackTrace();
        }
        logger.info("All threads are completed");
        service.shutdown();
    }
}