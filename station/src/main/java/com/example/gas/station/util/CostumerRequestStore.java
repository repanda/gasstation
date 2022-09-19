package com.example.gas.station.util;

import com.example.gas.station.CustomerRequest;
import com.example.gas.station.PumpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

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
        Thread dispatcherDiesel = new Thread(
                new GasDispatcher(this.dieselPendingRequests, pumpService::applyCostumerTransaction)
        );
        Thread dispatcherSuper = new Thread(
                new GasDispatcher(this.superPendingRequests, pumpService::applyCostumerTransaction)
        );
        Thread dispatcherRegular = new Thread(
                new GasDispatcher(this.regularPendingRequests, pumpService::applyCostumerTransaction)
        );

        dispatcherDiesel.setName("dispatcherDiesel");
        dispatcherDiesel.start();
        dispatcherSuper.setName("dispatcherSuper");
        dispatcherSuper.start();
        dispatcherRegular.setName("dispatcherRegular");
        dispatcherRegular.start();
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

// the consumer removes strings from the queue
class GasDispatcher implements Runnable {

    ConcurrentLinkedQueue<CustomerRequest> queue;

    Consumer<CustomerRequest> requestConsumer;

    GasDispatcher(ConcurrentLinkedQueue<CustomerRequest> queue, Consumer<CustomerRequest> requestConsumer) {
        this.queue = queue;
        this.requestConsumer = requestConsumer;
    }

    public void run() {
        while (true) {
            try {
                CustomerRequest request = queue.poll();
                if (request != null) {
                    requestConsumer.accept(request);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
