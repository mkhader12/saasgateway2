package com.virtru.saas.daemon;


import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtru.saas.constants.ApplicationConstants;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;


public class MessageMetaData implements Serializable {
    private String messageId;
    private String workingFileName;
    private String from;
    private String to;
    private String subject;
    private String status;
    private String receiptHandle;

    public MessageMetaData(Message inMsg, String inFileName) throws MessagingException {
        workingFileName = inFileName;
        messageId = extractMsgId(inMsg);
        from = extractFrom(inMsg.getFrom());
        to = extractTo(inMsg);
        subject = inMsg.getSubject();
    }

    public MessageMetaData() {

    }

    private String extractTo(Message aMsg) throws MessagingException {

        StringBuilder sb = new StringBuilder();
        String addresses = extractRecipient(aMsg, RecipientType.TO);
        if (addresses != null && !addresses.isEmpty()) {
            sb.append(addresses);
        }
        addresses = extractRecipient(aMsg, RecipientType.CC);
        if (addresses != null && !addresses.isEmpty()) {
            sb.append(addresses);
        }
        return sb.toString();
    }

    private String extractRecipient(Message aMsg, RecipientType recipientType) throws MessagingException {
        StringBuilder sb = new StringBuilder();
        Address[] toAddress = aMsg.getRecipients(recipientType);
        if (toAddress != null) {
            for (Address anAddress : toAddress) {
                sb.append(anAddress.toString()).append(",");
            }
        }
        return sb.toString();
    }

    private String extractFrom(Address[] from) {
        return from[0].toString();
    }

    private String extractMsgId(Message inMsg) throws MessagingException {
        String[] headers = inMsg.getHeader(ApplicationConstants.MESSAGE_ID);
        return headers[0];
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getWorkingFileName() {
        return workingFileName;
    }

    public void setWorkingFileName(String workingFileName) {
        this.workingFileName = workingFileName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiptHandle() {
        return receiptHandle;
    }

    public void setReceiptHandle(String receiptHandle) {
        this.receiptHandle = receiptHandle;
    }

    public String toTestJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.setReceiptHandle(null);
        return mapper.writeValueAsString(this);
    }


}
