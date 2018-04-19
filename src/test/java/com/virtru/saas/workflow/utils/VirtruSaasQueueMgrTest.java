package com.virtru.saas.workflow.utils;


import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.virtru.saas.constants.SqsQueueNames;
import com.virtru.saas.daemon.MessageMetaData;
import com.virtru.saas.framework.sqs.VirtruSaasQueueMgr;
import javax.jms.JMSException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class VirtruSaasQueueMgrTest {
    private static VirtruSaasQueueMgr queueMgr;

    @BeforeClass
    public static void appSetup() throws JMSException {
        queueMgr = VirtruSaasQueueMgr.getInstance();
    }

    @Before
    public void setup() {
    }


    @Test
    public void queueBootstrapTest() {
        Map<String, String> sqsQueues = queueMgr.getSqsQueues();
        assertNotNull(sqsQueues);
        assertEquals(3, sqsQueues.size());
        assertTrue(sqsQueues.containsKey(SqsQueueNames.PROCESSING_Q.toString()));
        assertTrue(sqsQueues.containsKey(SqsQueueNames.RETRY_Q.toString()));
        assertTrue(sqsQueues.containsKey(SqsQueueNames.DELIVERY_Q.toString()));
    }

    @Test
    public void addQueueTest() throws JsonProcessingException, JMSException, InterruptedException {
        MessageMetaData metaData = new MessageMetaData();
        metaData.setWorkingFileName("/file/message1");
        metaData.setFrom("mkhader@virtruprivacy.com");
        metaData.setTo("karthik@virtruprivacy.com");
        metaData.setSubject("1A Mail Subject from Junit addQueuetest");
        metaData.setStatus(SqsQueueNames.PROCESSING_Q.name());
        queueMgr.addDataToQueue(SqsQueueNames.PROCESSING_Q, metaData);
        List<MessageMetaData> objectsFromQueue = null;
        int i=0;
        while (i < 20 && objectsFromQueue == null) {
            objectsFromQueue =
                    queueMgr.getDataFromQueue(SqsQueueNames.PROCESSING_Q);
            Thread.sleep(1000);
        }
        assertNotNull(objectsFromQueue);
        assertTrue(objectsFromQueue.size() > 0);
        MessageMetaData objectFromQueue = objectsFromQueue.get(0);
        String storedReceiptHandle = objectFromQueue.getReceiptHandle();
        assertEquals(metaData.toTestJson(), objectFromQueue.toTestJson());
        objectFromQueue.setReceiptHandle(storedReceiptHandle);

       queueMgr.removeDataFromQueue(SqsQueueNames.PROCESSING_Q, objectFromQueue);


    }



}
