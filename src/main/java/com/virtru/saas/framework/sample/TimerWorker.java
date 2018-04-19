package com.virtru.saas.framework.sample;


import com.virtru.saas.constants.WorkerStatus;
import com.virtru.saas.framework.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TimerWorker implements Worker {
    private static final Logger LOG = LogManager.getLogger(TimerWorker.class);
    private WorkerStatus workerStatus;

    @Override
    public void performWork(Object requestObject) {
        LOG.info(getName() +" Processing Work - " + requestObject);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getName() {
        return getClass().getName() +"-"+ Thread.currentThread().getName();
    }
}
