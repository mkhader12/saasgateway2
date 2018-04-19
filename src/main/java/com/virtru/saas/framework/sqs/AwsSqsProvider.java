package com.virtru.saas.framework.sqs;


import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import javax.jms.JMSException;


public class AwsSqsProvider implements QueueProvider {
    private static AmazonSQSMessagingClientWrapper client;

    public AwsSqsProvider(int numberOfMessagesToPrefetch) throws JMSException {
        // Create a new connection factory with all defaults (credentials and region) set automatically
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration().withNumberOfMessagesToPrefetch(numberOfMessagesToPrefetch),
                AmazonSQSClientBuilder.defaultClient()
        );
        // Create the connection.
        SQSConnection connection = connectionFactory.createConnection();
        // Get the wrapped client
        client = connection.getWrappedAmazonSQSClient();
    }

    @Override
    public void deleteMessage(DeleteMessageRequest deleteMessageRequest) throws JMSException {
            client.deleteMessage(deleteMessageRequest);
    }

    @Override
    public SendMessageResult sendMessage(SendMessageRequest sendMessageRequest) throws JMSException {
        return client.sendMessage(sendMessageRequest);
    }

    @Override
    public boolean queueExists(String queueName) throws JMSException {
        return client.queueExists(queueName);
    }

    @Override
    public GetQueueUrlResult getQueueUrl(String queueName) throws JMSException {
        return client.getQueueUrl(queueName);
    }


    @Override
    public CreateQueueResult createQueue(CreateQueueRequest createQueueRequest) {
        return null;
    }

    @Override
    public ReceiveMessageResult receiveMessage(ReceiveMessageRequest receiveMessageRequest) throws JMSException {
        return client.receiveMessage(receiveMessageRequest);
    }
}
