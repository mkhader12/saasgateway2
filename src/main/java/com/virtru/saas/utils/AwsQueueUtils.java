package com.virtru.saas.utils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.virtru.saas.framework.sqs.AwsSqsProvider;
import com.virtru.saas.framework.sqs.QueueProvider;
import javax.jms.JMSException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AwsQueueUtils  {
    private static final Logger LOG = LogManager.getLogger(AwsQueueUtils.class);
    private static AwsQueueUtils instance;
    private QueueProvider queueProvider;

    public static AwsQueueUtils getInstance(int numberOfMessagesToPrefetch) throws JMSException {
        if (instance == null) {
            instance = new AwsQueueUtils(numberOfMessagesToPrefetch);
        }
        return instance;
    }

    public AwsQueueUtils(int msgToRetrieve) throws JMSException {
        this.queueProvider = getQueueProvider(msgToRetrieve)                                                                                                                                                                                                                                                    ;

    }

    protected QueueProvider getQueueProvider(int msgToRetrieve) throws JMSException {
        return new AwsSqsProvider(msgToRetrieve);
    }

    public void sendMessageToQueue(String queueUrl, String groupId, String jsonString) throws JMSException {
        LOG.info("Adding to AWS SQS Queue");

        // Send a message
        LOG.info("Sending a message to " + queueUrl + "\n");
        final SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, jsonString);

        // When you send messages to a FIFO queue,  MessageGroupId is required
        sendMessageRequest.setMessageGroupId(groupId);

        // Send Message
        final SendMessageResult sendMessageResult = queueProvider.sendMessage(sendMessageRequest);
        final String sequenceNumber = sendMessageResult.getSequenceNumber();
        final String messageId = sendMessageResult.getMessageId();

        LOG.info(
                "SendMessage succeed with messageId " + messageId + ", sequence number " + sequenceNumber + "\n");
    }

    public List<Message> receiveMessageFromQueue(String myQueueUrl, Integer visibilityTimeout)
            throws JMSException {
        // Receive messages
        final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
        receiveMessageRequest.setVisibilityTimeout(visibilityTimeout);
        final List<Message> messages = queueProvider.receiveMessage(receiveMessageRequest).getMessages();

        for (final Message message : messages) {
            LOG.info("Message");
            LOG.info("Receiving messages from "+ myQueueUrl +"\n");
            LOG.info("  MessageId:     " + message.getMessageId());
            /*LOG.info("  ReceiptHandle: " + message.getReceiptHandle());
            LOG.info("  MD5OfBody:     " + message.getMD5OfBody());
            LOG.info("  Body:          " + message.getBody());
            for (final Entry<String, String> entry : message.getAttributes().entrySet()) {
                LOG.info("Attribute");
                LOG.info("  Name:  " + entry.getKey());
                LOG.info("  Value: " + entry.getValue());
            }
            */

        }
        return messages;
    }

    public void deleteMessageFromQueue(String myQueueUrl, String messageReceiptHandle) throws JMSException {
        // Delete the message
        final DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(myQueueUrl, messageReceiptHandle);
        queueProvider.deleteMessage(deleteMessageRequest);
    }

    public String createFifoQueue(String queueName) throws JMSException {
        if (!queueName.endsWith(".fifo")) {
            queueName += ".fifo";
        }
        // Create an Amazon SQS FIFO queue named MyQueue.fifo, if it doesn't already exist
        if (!queueProvider.queueExists(queueName)) {
            LOG.info("Creating a new Amazon SQS FIFO queue [" + queueName + "]\n");
            Map<String, String> attributes = new HashMap<>();
            attributes.put("FifoQueue", "true");
            attributes.put("ContentBasedDeduplication", "true");
            attributes.put("VisibilityTimeout", "60");
            CreateQueueResult queue = queueProvider.createQueue(
                    new CreateQueueRequest().withQueueName(queueName).withAttributes(attributes));
            return queue.getQueueUrl();
        }
        LOG.info("Amazon SQS FIFO queue [" + queueName + "] already exists\n");
        return queueProvider.getQueueUrl(queueName).getQueueUrl();
    }


}
