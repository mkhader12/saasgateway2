package com.virtru.saas.testutils;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.virtru.saas.app.ApplicationContext;
import com.virtru.saas.framework.FilePoller;
import com.virtru.saas.framework.Poller;
import com.virtru.saas.framework.sqs.QueueProvider;
import com.virtru.saas.framework.sqs.VirtruSaasQueueMgr;
import com.virtru.saas.utils.AwsQueueUtils;
import javax.jms.JMSException;


public class SwissArmyKnife {

    public static Poller getSingleFileTestPoller(String fileName) throws IOException {
        ApplicationContext appCtx = getTestApplicationCtx();
        Poller singleFileTestPoller = new FilePoller(appCtx) {

            @Override
            protected Object getPollingRequestObject() {
                return new File(fileName);
            }

            @Override
            public String getFilePath() {
                return System.getProperty("user.home");
            }
        };
        return singleFileTestPoller;
    }

    public static Poller getMultipleFileTestPoller(String[] fileNames) throws IOException {
        ApplicationContext appCtx = getTestApplicationCtx();
        Poller singleFileTestPoller = new FilePoller(appCtx) {

            @Override
            protected Object getPollingRequestObject() {
                List<File> fileList = new ArrayList<>();
                for (String filename : fileNames) {
                    fileList.add(new File(filename));
                }
                return fileList;
            }

            @Override
            public String getFilePath() {
                return System.getProperty("user.home");
            }
        };
        return singleFileTestPoller;
    }


    public static VirtruSaasQueueMgr getTestVirtruSaasQueueMgr() throws JMSException {

        VirtruSaasQueueMgr testQueueMgr = new VirtruSaasQueueMgr() {
            @Override
            protected AwsQueueUtils getAwsQueueUtils(int numberOfMessagesToPrefetch) throws JMSException {
                return SwissArmyKnife.getAwsQueueUtils(numberOfMessagesToPrefetch);
            }
        };

        return testQueueMgr;
    }

    private static AwsQueueUtils getAwsQueueUtils(int numberOfMessagesToPrefetch) throws JMSException {
        AwsQueueUtils awsQueueUtils = new AwsQueueUtils(numberOfMessagesToPrefetch) {
            @Override
            protected QueueProvider getQueueProvider(int msgToRetrieve)  {
                return SwissArmyKnife.getTestQueueProvider();
            }
        };
        return awsQueueUtils;
    }

    private static QueueProvider getTestQueueProvider() {
        QueueProvider queueProvider = new QueueProvider() {
            @Override
            public void deleteMessage(DeleteMessageRequest deleteMessageRequest)  {
                if (deleteMessageRequest != null) {
                    TestExecutionContext testExecutionContext = TestExecutionContext.getInstance();
                    testExecutionContext.setDeleteQueueMsgRequest(deleteMessageRequest);
                }
            }

            @Override
            public SendMessageResult sendMessage(SendMessageRequest sendMessageRequest){
                if (sendMessageRequest != null) {
                    TestExecutionContext testExecutionContext = TestExecutionContext.getInstance();
                    testExecutionContext.setSendQueueMsgRequest(sendMessageRequest);
                    return testExecutionContext.getSendQueueMsgResponse();
                }
                return null;
            }

            @Override
            public boolean queueExists(String queueName)  {
                TestExecutionContext testExecutionContext = TestExecutionContext.getInstance();
                return testExecutionContext.queueExists(queueName);
            }

            @Override
            public GetQueueUrlResult getQueueUrl(String queueName)  {
                TestExecutionContext testExecutionContext = TestExecutionContext.getInstance();
                return TestExecutionContext.GetQueueUrlResult(queueName);
            }

            @Override
            public CreateQueueResult createQueue(CreateQueueRequest createQueueRequest) {
                return TestExecutionContext.createQueue(createQueueRequest);
            }

            @Override
            public ReceiveMessageResult receiveMessage(ReceiveMessageRequest receiveMessageRequest)
                    throws JMSException {
                TestExecutionContext testExecutionContext = TestExecutionContext.getInstance();
                return testExecutionContext.getReceiveMessageResult(receiveMessageRequest);
            }
        };
        return queueProvider;
    }

    private static ApplicationContext getTestApplicationCtx() {
        return null;
    }
}
