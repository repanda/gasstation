package com.example.gas.station.util;

import com.example.gas.station.CustomerRequest;
import com.example.gas.station.PumpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CostumerRequestStore {

    private Logger logger = LoggerFactory.getLogger(CostumerRequestStore.class);

    private ConcurrentLinkedQueue<CustomerRequest> pendingRequests = new ConcurrentLinkedQueue();

    private final PumpService pumpService;

    public CostumerRequestStore(PumpService pumpService) {
        this.pumpService = pumpService;
        initThread();
    }


    private void initThread() {
        Thread dispatcher = new Thread(() -> {
            while (true) {
                try {
                    CustomerRequest request = pendingRequests.poll();
                    if (request != null) {
                        pumpService.applyCostumerTransaction(request);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dispatcher.setName("Dispatcher");
        dispatcher.start();
    }


    // this method queues a message delivery request
    public void publishRequest(CustomerRequest request) {
        try {
            pendingRequests.add(request);
        } catch (Exception e) {
            throw e;
        }
    }
}
