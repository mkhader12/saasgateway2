package com.virtru.saas.daemon.pickup;


import java.io.IOException;

import com.virtru.saas.app.ApplicationContext;
import com.virtru.saas.framework.Poller;
import com.virtru.saas.testutils.SwissArmyKnife;
import javax.jms.JMSException;
import org.junit.Before;


public class PickupDaemonTest {

    PickupDaemon pickupDaemon;

    @Before
    public void setup() throws JMSException {
        ApplicationContext appctx=new ApplicationContext() ;
        String fileName="";


        pickupDaemon = new PickupDaemon(appctx) {
            @Override
            protected Poller getPoller() {
                try {
                    return SwissArmyKnife.getSingleFileTestPoller(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

}