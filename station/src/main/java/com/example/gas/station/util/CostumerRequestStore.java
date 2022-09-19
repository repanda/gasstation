package com.example.gas.station.util;

import com.example.gas.station.CustomerRequest;
import com.example.gas.station.PumpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CostumerRequestStore {

    private Logger logger = LoggerFactory.getLogger(CostumerRequestStore.class);

    private ConcurrentLinkedQueue<CustomerRequest> dieselPendingRequests = new ConcurrentLinkedQueue();
    private ConcurrentLinkedQueue<CustomerRequest> superPendingRequests = new ConcurrentLinkedQueue();
    private ConcurrentLinkedQueue<CustomerRequest> regularPendingRequests = new ConcurrentLinkedQueue();

    private final PumpService pumpService;

    public CostumerRequestStore(PumpService pumpService) {
        this.pumpService = pumpService;
        initThread();
    }


    private void initThread() {
        Thread dispatcherDiesel = new Thread(() -> {
            while (true) {
                try {
                    CustomerRequest request = dieselPendingRequests.poll();
                    if (request != null) {
                        pumpService.applyCostumerTransaction(request);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread dispatcherSuper = new Thread(() -> {
            while (true) {
                try {
                    CustomerRequest request = superPendingRequests.poll();
                    if (request != null) {
                        pumpService.applyCostumerTransaction(request);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dispatcherDiesel.setName("dispatcherDiesel");
        dispatcherDiesel.start();
        dispatcherSuper.setName("dispatcherSuper");
        dispatcherSuper.start();
    }


    // this method queues a costumer request
    public void publishRequest(CustomerRequest request) {
        try {
            switch (request.gasType()) {
                case DIESEL -> dieselPendingRequests.add(request);
                case SUPER -> superPendingRequests.add(request);
                case REGULAR -> regularPendingRequests.add(request);
            }
        } catch (Exception e) {
            throw e;
        }
    }
}

