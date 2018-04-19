package com.virtru.saas.utils;


import com.virtru.saas.daemon.MessageMetaData;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


public class VirtruMessageUtil {
    public static MessageMetaData getMetaData(MimeMessage message, String fileName) throws MessagingException {
        return new MessageMetaData(message, fileName);
    }

    public static boolean isBounceMessage(MimeMessage message) {
        return false;
    }
}
