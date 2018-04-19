package com.virtru.saas.framework.sqs;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtru.saas.constants.SqsQueueNames;
import com.virtru.saas.daemon.MessageMetaData;
import com.virtru.saas.utils.AwsQueueUtils;
import javax.jms.JMSException;


public class VirtruSaasQueueMgr {
    private static VirtruSaasQueueMgr instance;
    private Map<String, String> sqsQueues = new HashMap<>();
    private AwsQueueUtils util;

    public static VirtruSaasQueueMgr getInstance() throws JMSException {
        if (instance == null) {
            instance = new VirtruSaasQueueMgr();
        }
        return instance;
    }

    public VirtruSaasQueueMgr() throws JMSException {
        // Read it from config // TODO

        int numberOfMessagesToPrefetch = 5; //TODO - REPLACE WITH CONFIG

        util = getAwsQueueUtils(numberOfMessagesToPrefetch);
        bootstrapQueues();
    }

    protected AwsQueueUtils getAwsQueueUtils(int numberOfMessagesToPrefetch) throws JMSException {
        return AwsQueueUtils.getInstance(numberOfMessagesToPrefetch);
    }

    private void bootstrapQueues() throws JMSException {
        createQueue(SqsQueueNames.PROCESSING_Q);
        createQueue(SqsQueueNames.RETRY_Q);
        createQueue(SqsQueueNames.DELIVERY_Q);
        createQueue(SqsQueueNames.BOUNCE_Q);
    }

    public void createQueue(SqsQueueNames queueName) throws JMSException {
        String queueUrl = util.createFifoQueue(queueName.toString());
        if (queueUrl != null) {
            sqsQueues.put(queueName.toString(), queueUrl);
        }
    }

    public void addDataToQueue(SqsQueueNames queueNames, MessageMetaData metaMessage)
            throws JsonProcessingException, JMSException {
        String queueUrl = sqsQueues.get(queueNames.toString());
        if (queueUrl != null) {
            ObjectMapper mapper = new ObjectMapper();
            util.sendMessageToQueue(queueUrl, queueNames.name(), mapper.writeValueAsString(metaMessage));
        }
    }

    public List<MessageMetaData> getDataFromQueue(SqsQueueNames queueNames)
            throws JMSException {
        String queueUrl = sqsQueues.get(queueNames.toString());
        if (queueUrl != null) {
            Integer readMessageTimeout = 60;
            List<Message> jsonMsgs = util.receiveMessageFromQueue(queueUrl, readMessageTimeout);
            if (jsonMsgs != null && jsonMsgs.size() > 0) {
                List<MessageMetaData> returnList = new ArrayList<>();
                jsonMsgs.forEach((Message aJsonMsg) -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                        MessageMetaData obj = mapper.readValue(aJsonMsg.getBody(), MessageMetaData.class);
                        obj.setReceiptHandle(aJsonMsg.getReceiptHandle());
                        returnList.add(obj);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                return returnList;
            }

        }
        return null;
    }

    public Map<String, String> getSqsQueues() {
        return sqsQueues;
    }

    public void removeDataFromQueue(SqsQueueNames queueNames, MessageMetaData objectFromQueue) throws JMSException {
        String queueUrl = sqsQueues.get(queueNames.toString());

        if (queueUrl != null) {
            util.deleteMessageFromQueue(queueUrl, objectFromQueue.getReceiptHandle());
        }

    }
}

