package com.virtru.saas.daemon.processing;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.virtru.saas.constants.RequestStatus;
import com.virtru.saas.constants.SqsQueueNames;
import com.virtru.saas.daemon.MessageMetaData;
import com.virtru.saas.daemon.ProcessResult;
import com.virtru.saas.framework.sqs.SqsDaemonWorker;
import javax.jms.JMSException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ProcessingDaemonWorker extends SqsDaemonWorker {
    private static final Logger LOG = LogManager.getLogger(ProcessingDaemonWorker.class);

    public ProcessingDaemonWorker()  {

    }

    protected void processMsg(MessageMetaData msgData) throws JsonProcessingException, JMSException {

        ProcessResult result = processDlp(msgData);
        if (result.isSuccess()) {
            msgData.setStatus(RequestStatus.PROCESS_COMPLETED.toString());
            addToSqsQueue(SqsQueueNames.DELIVERY_Q, msgData);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            deleteFromSqsQueue(SqsQueueNames.PROCESSING_Q, msgData);
        }
        else
        {
            msgData.setStatus(RequestStatus.FAILED_IN_PROCESSING.toString());

        }

    }

    private ProcessResult processDlp (MessageMetaData msg) {

        ProcessResult result = new ProcessResult();
        result.setSuccess(true); // Force this for now until dlp is implemented

        return result;
    }

}

