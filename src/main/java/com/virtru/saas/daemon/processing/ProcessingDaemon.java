package com.virtru.saas.daemon.processing;


import java.io.IOException;

import com.virtru.saas.app.ApplicationContext;
import com.virtru.saas.config.DefaultApplicationConfig;
import com.virtru.saas.framework.AbstractDaemon;
import com.virtru.saas.framework.Poller;
import com.virtru.saas.framework.Worker;
import javax.jms.JMSException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ProcessingDaemon extends AbstractDaemon {
    private static final Logger LOG = LogManager.getLogger(ProcessingDaemon.class);


    public ProcessingDaemon(
            ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    protected Poller getPoller() {
        try {
            return new ProcessingDaemonSqsPoller(getAppCtx());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Worker getProcessWorker()  {
        return new ProcessingDaemonWorker();
    }

    @Override
    public String getName() {
        return getClass().getName();
    }


    public static void main(String[] args) throws JMSException {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.setDefaultConfig(new DefaultApplicationConfig());
        ProcessingDaemon processingDaemon = new ProcessingDaemon(applicationContext);
        processingDaemon.start();
    }

}

