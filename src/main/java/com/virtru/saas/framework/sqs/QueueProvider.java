package com.virtru.saas.framework.sqs;


import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import javax.jms.JMSException;


public interface QueueProvider {

        void deleteMessage(DeleteMessageRequest deleteMessageRequest) throws JMSException;

        SendMessageResult sendMessage(SendMessageRequest sendMessageRequest) throws JMSException;

        boolean queueExists(String queueName) throws JMSException;

        GetQueueUrlResult getQueueUrl(String queueName) throws JMSException;

        CreateQueueResult createQueue(CreateQueueRequest createQueueRequest) throws JMSException;

        ReceiveMessageResult receiveMessage(ReceiveMessageRequest receiveMessageRequest) throws JMSException;


    }
