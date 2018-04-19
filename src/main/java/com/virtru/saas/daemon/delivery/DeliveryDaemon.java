package com.virtru.saas.daemon.delivery;


import java.io.IOException;

import com.virtru.saas.app.ApplicationContext;
import com.virtru.saas.config.DefaultApplicationConfig;
import com.virtru.saas.framework.AbstractDaemon;
import com.virtru.saas.framework.Poller;
import com.virtru.saas.framework.Worker;
import javax.jms.JMSException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DeliveryDaemon extends AbstractDaemon {
    private static final Logger LOG = LogManager.getLogger(DeliveryDaemon.class);


    public DeliveryDaemon(
            ApplicationContext applicationContext)  {
        super(applicationContext);
    }

    @Override
    protected Poller getPoller() {
        try {
            return new DeliveryDaemonSqsPoller(getAppCtx());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Worker getProcessWorker()  {
        return new DeliveryDaemonWorker();
    }

    @Override
    public String getName() {
        return getClass().getName();
    }


    public static void main(String[] args) throws JMSException {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.setDefaultConfig(new DefaultApplicationConfig());
        DeliveryDaemon
                pickupDaemon = new DeliveryDaemon(applicationContext);
        pickupDaemon.start();
    }


}

