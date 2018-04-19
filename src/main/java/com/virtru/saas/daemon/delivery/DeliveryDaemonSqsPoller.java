package com.virtru.saas.daemon.delivery;


import java.io.IOException;

import com.virtru.saas.app.ApplicationContext;
import com.virtru.saas.constants.SqsQueueNames;
import com.virtru.saas.framework.sqs.SqsPoller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DeliveryDaemonSqsPoller extends SqsPoller {
    private static final Logger LOG = LogManager.getLogger(DeliveryDaemonSqsPoller.class);


    public DeliveryDaemonSqsPoller(ApplicationContext applicationContext) throws IOException {
        super(applicationContext);
    }

    @Override
    protected SqsQueueNames getSqsName() {
        return SqsQueueNames.DELIVERY_Q;
    }


}