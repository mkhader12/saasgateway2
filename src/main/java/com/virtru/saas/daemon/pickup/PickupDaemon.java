package com.virtru.saas.daemon.pickup;


import java.io.IOException;

import com.virtru.saas.app.ApplicationContext;
import com.virtru.saas.config.DefaultApplicationConfig;
import com.virtru.saas.framework.AbstractDaemon;
import com.virtru.saas.framework.Poller;
import com.virtru.saas.framework.Worker;
import javax.jms.JMSException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PickupDaemon extends AbstractDaemon {
    private static final Logger LOG = LogManager.getLogger(PickupDaemon.class);


    public PickupDaemon(
            ApplicationContext applicationContext) throws JMSException {
        super(applicationContext);
    }

    @Override
    protected Poller getPoller() {
        try {
            return new PickupDaemonFilePoller(getAppCtx());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Worker getProcessWorker()  {
        return new PickupDaemonWorker();
    }

    @Override
    public String getName() {
        return getClass().getName();
    }


    public static void main(String[] args) throws JMSException {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.setDefaultConfig(new DefaultApplicationConfig());
        PickupDaemon pickupDaemon = new PickupDaemon(applicationContext);
        pickupDaemon.start();
    }


}

