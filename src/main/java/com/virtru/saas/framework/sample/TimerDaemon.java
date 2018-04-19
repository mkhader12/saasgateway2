package com.virtru.saas.framework.sample;


import com.virtru.saas.app.ApplicationContext;
import com.virtru.saas.framework.AbstractDaemon;
import com.virtru.saas.framework.Poller;
import com.virtru.saas.framework.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TimerDaemon extends AbstractDaemon {
    private static final Logger LOG = LogManager.getLogger(TimerDaemon.class);

    public TimerDaemon(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    protected Worker getProcessWorker() {
        return new TimerWorker();
    }

    @Override
    protected Poller getPoller() {
        return new TimePoller(getAppCtx());
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    public static void main (String[] args) throws InterruptedException {
        TimerDaemon daemon = new TimerDaemon(new ApplicationContext());
        daemon.start();
        Thread.currentThread().join();
    }
}
