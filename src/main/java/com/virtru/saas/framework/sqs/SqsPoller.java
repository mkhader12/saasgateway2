package com.virtru.saas.framework.sqs;


import java.io.IOException;
import java.nio.file.Path;

import com.virtru.saas.app.ApplicationContext;
import com.virtru.saas.constants.SqsQueueNames;
import com.virtru.saas.framework.AbstractPoller;
import javax.jms.JMSException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class SqsPoller extends AbstractPoller {
    private static final Logger LOG = LogManager.getLogger(SqsPoller.class);
    private Path monitorQueuePath;
    private VirtruSaasQueueMgr queueMgr;

    public SqsPoller(ApplicationContext applicationContext) {
        super(applicationContext);
        try {
            queueMgr = VirtruSaasQueueMgr.getInstance();
        } catch (JMSException e) {
            LOG.error(e);
        }

    }

    protected abstract SqsQueueNames getSqsName();

    @Override
    protected Object getPollingRequestObject() {
        try {
            return queueMgr.getDataFromQueue(getSqsName());
        } catch (JMSException e) {
            LOG.error(e);
        }
        return null;
    }

}
