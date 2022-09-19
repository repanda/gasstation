package com.example.gas.station;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiRequestRunner {
    Logger logger = LoggerFactory.getLogger(MultiRequestRunner.class);

    public MultiRequestRunner() {
    }

    public void run(Callable<Double>... callables) {
        List<Callable<Double>> futureList = Arrays.stream(callables).toList();

        ExecutorService service = Executors.newFixedThreadPool(4);
        logger.info("Start 4 threads at the same time");
        try {
            service.invokeAll(futureList); // wait for all threads to complete
        } catch (Exception err) {
            err.printStackTrace();
        }
        logger.info("All threads are completed");
        try {
            Thread dispatcher = getThreadByName("Dispatcher"); // wait for event dispatcher thread to complete
            dispatcher.join(10000);
            logger.info("Dispatcher thread is completed");
        } catch (Exception err) {
            err.printStackTrace();
        }
        service.shutdown();
    }
    public Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) return t;
        }
        return null;
    }
}