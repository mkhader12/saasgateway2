package com.virtru.saas.daemon.processing;


import java.io.IOException;

import com.virtru.saas.app.ApplicationContext;
import com.virtru.saas.constants.SqsQueueNames;
import com.virtru.saas.framework.sqs.SqsPoller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ProcessingDaemonSqsPoller extends SqsPoller {
    private static final Logger LOG = LogManager.getLogger(ProcessingDaemonSqsPoller.class);


    public ProcessingDaemonSqsPoller(ApplicationContext applicationContext) throws IOException {
        super(applicationContext);
    }

    @Override
    protected SqsQueueNames getSqsName() {
        return SqsQueueNames.PROCESSING_Q;
    }


}