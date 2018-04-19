package com.virtru.saas.config;


public class PickupDaemonConfig extends DaemonConfig {
    private String messageFilesFolderPath;

    public String getMessageFilesFolderPath() {
        return messageFilesFolderPath;
    }

    public void setMessageFilesFolderPath(String messageFilesFolderPath) {
        this.messageFilesFolderPath = messageFilesFolderPath;
    }
}
