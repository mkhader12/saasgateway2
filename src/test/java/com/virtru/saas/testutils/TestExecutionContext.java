package com.virtru.saas.testutils;


import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;


public class TestExecutionContext {
    public static TestExecutionContext getInstance() {
        return null;
    }

    public void setDeleteQueueMsgRequest(DeleteMessageRequest deleteMessageRequest) {

    }

    public void setSendQueueMsgRequest(SendMessageRequest sendMessageRequest) {

    }

    public SendMessageResult getSendQueueMsgResponse() {
        return null;
    }

    public boolean queueExists(String queueName) {
        return false;
    }

    public static GetQueueUrlResult GetQueueUrlResult(String queueName) {
        return null;
    }

    public static CreateQueueResult createQueue(CreateQueueRequest createQueueRequest) {
        return null;
    }

    public ReceiveMessageResult getReceiveMessageResult(ReceiveMessageRequest receiveMessageRequest) {
        return null;
    }
}
