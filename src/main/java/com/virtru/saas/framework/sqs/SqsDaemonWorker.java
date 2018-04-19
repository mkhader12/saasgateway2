package com.virtru.saas.framework.sqs;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.virtru.saas.constants.SqsQueueNames;
import com.virtru.saas.daemon.MessageMetaData;
import com.virtru.saas.framework.Worker;
import javax.jms.JMSException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class SqsDaemonWorker implements Worker {
    private static final Logger LOG = LogManager.getLogger(SqsDaemonWorker.class);
    private VirtruSaasQueueMgr saasQueueMgr;

    public SqsDaemonWorker()  {
        try {
            saasQueueMgr = getVirtruSaasQueueMgr();
        } catch (JMSException e) {
            LOG.error(e);
        }
    }

    protected VirtruSaasQueueMgr getVirtruSaasQueueMgr() throws JMSException {
        return VirtruSaasQueueMgr.getInstance();
    }

    protected final void addToSqsQueue(SqsQueueNames queueName, MessageMetaData data) throws JMSException, JsonProcessingException {
        LOG.info(getName() + " - addSqsQueue");
        saasQueueMgr.addDataToQueue(queueName, data);
    }


    protected void deleteFromSqsQueue(SqsQueueNames queueNames, MessageMetaData msgData) throws JMSException {
        LOG.info(getName() + " - delete From SQS Queue");
        saasQueueMgr.removeDataFromQueue(queueNames, msgData);
    }

    @Override
    public void performWork(Object requestObject) {
        LOG.info(getName()+" Got the work - "+ requestObject.toString());
        try {
            processQueueMsg(requestObject);
        } catch (IOException e) {
            LOG.error(e);
        } catch (JMSException e1) {
            LOG.error(e1);
        }
    }


    protected void processQueueMsg(Object requestObject) throws IOException, JMSException {
        MessageMetaData msgData;

        if (requestObject instanceof ArrayList) {
            List<MessageMetaData> arrayList = (List) requestObject;
            for (MessageMetaData msg : arrayList) {
                processMsg(msg);
            }
        }
        else if (requestObject instanceof MessageMetaData) {
            msgData = (MessageMetaData) requestObject;
            processMsg(msgData);
        }
        if (requestObject instanceof File) {
            msgData = new MessageMetaData();
            File aFile = (File) requestObject;
            msgData.setWorkingFileName(aFile.getAbsolutePath());
            processMsg(msgData);
        }
    }

    protected abstract void processMsg(MessageMetaData msg) throws JsonProcessingException, JMSException;

    @Override
    public String getName() {
        return getClass().getName();
    }

}

