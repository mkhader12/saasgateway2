package com.virtru.saas.daemon.delivery;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.virtru.saas.constants.RequestStatus;
import com.virtru.saas.daemon.MessageMetaData;
import com.virtru.saas.framework.sqs.SqsDaemonWorker;
import javax.jms.JMSException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DeliveryDaemonWorker extends SqsDaemonWorker {
    private static final Logger LOG = LogManager.getLogger(DeliveryDaemonWorker.class);

    @Override
    protected void processMsg(MessageMetaData msg)  {
        msg.setStatus(RequestStatus.READY_FOR_DELIVERY.toString());
        deliverMsg(msg);
    }

    private void deliverMsg(MessageMetaData msg) {

        // Have delivery logic here

    }


    @Override
    public String getName() {
        return getClass().getName();
    }
}

