package com.virtru.saas.app;


import java.util.ArrayList;
import java.util.List;

import com.virtru.saas.config.DefaultApplicationConfig;
import com.virtru.saas.daemon.delivery.DeliveryDaemon;
import com.virtru.saas.daemon.pickup.PickupDaemon;
import com.virtru.saas.daemon.processing.ProcessingDaemon;
import com.virtru.saas.framework.AbstractDaemon;
import javax.jms.JMSException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SaasGatewayApp  {
    private static final Logger LOG = LogManager.getLogger(SaasGatewayApp.class);

    public List<AbstractDaemon> getDaemons() {
        return daemons;
    }

    private List<AbstractDaemon> daemons = new ArrayList<>();


    public SaasGatewayApp(ApplicationContext appCtx) throws JMSException {
        LOG.info("Initializing Gateway App");
        addDaemon(new PickupDaemon(appCtx));
        addDaemon(new ProcessingDaemon(appCtx));
        addDaemon(new DeliveryDaemon(appCtx));
    }

    private void addDaemon(AbstractDaemon daemon) {
        daemons.add(daemon);
    }


    public static void main(String[] args) throws InterruptedException, JMSException {
        ApplicationContext appCtx = new ApplicationContext();
        appCtx.setDefaultConfig(new DefaultApplicationConfig());
        SaasGatewayApp app = new SaasGatewayApp(appCtx);
        LOG.info("Starting Worker App");

        app.startApp();
    }

    private void startApp() throws InterruptedException {
        for (AbstractDaemon aDaemon : daemons) {
            aDaemon.start();
        }
        Thread.currentThread().join();
    }


}
