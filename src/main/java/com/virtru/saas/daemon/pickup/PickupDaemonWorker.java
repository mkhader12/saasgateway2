package com.virtru.saas.daemon.pickup;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import com.virtru.saas.constants.RequestStatus;
import com.virtru.saas.constants.SqsQueueNames;
import com.virtru.saas.daemon.MessageMetaData;
import com.virtru.saas.daemon.ProcessResult;
import com.virtru.saas.framework.sqs.SqsDaemonWorker;
import com.virtru.saas.utils.MailDeliveryUtil;
import com.virtru.saas.utils.VirtruMessageUtil;
import javax.jms.JMSException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PickupDaemonWorker extends SqsDaemonWorker {
    private static final Logger LOG = LogManager.getLogger(PickupDaemonWorker.class);

    protected void processMsg(MessageMetaData msg) {
        ProcessResult result = new ProcessResult();
        final Properties props = new Properties();
        String fileName = msg.getWorkingFileName();
        try (InputStream is = Files.newInputStream(Paths.get(fileName))) {
            // First Parse the message
            MimeMessage message = new MimeMessage(Session.getDefaultInstance(props, null), is);

            // Check to see if the message is in the bounce path
            // If the message is for bounce then bounce message
            if (isBounceMessage(message, fileName)) {
                BounceMessage(message);
            } else {
                MessageMetaData msgMetaData = VirtruMessageUtil.getMetaData(message, fileName);
                msgMetaData.setStatus(RequestStatus.READY_FOR_PROCESSING.toString());
                addToSqsQueue(SqsQueueNames.PROCESSING_Q, msgMetaData);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            result.setException(e);
        } catch (JMSException e1) {
            e1.printStackTrace();
            result.setException(e1);
        } catch (IOException e2) {
            e2.printStackTrace();
            result.setException(e2);
        }
    }


    private void BounceMessage(MimeMessage message) {
        MailDeliveryUtil.bounceMessage(message);
    }

    protected boolean isBounceMessage(MimeMessage message, String fileName) throws MessagingException {
        MessageMetaData msgMetaData = VirtruMessageUtil.getMetaData(message, fileName);
        msgMetaData.setStatus(RequestStatus.BOUNCED_AFTER_PICKUP.toString());
        return VirtruMessageUtil.isBounceMessage(message);
    }

    @Override
    public String getName() {
        return getClass().getName();
    }
}

