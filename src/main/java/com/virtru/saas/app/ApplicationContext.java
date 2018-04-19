package com.virtru.saas.app;


import com.virtru.saas.config.AppConfig;
import com.virtru.saas.config.DaemonConfig;
import com.virtru.saas.config.DefaultApplicationConfig;
import com.virtru.saas.config.PickupDaemonConfig;


public class ApplicationContext {

    private AppConfig appConfig;
    private DaemonConfig processingDaemonConfig;

    public ApplicationContext() {
        appConfig= new AppConfig();
        processingDaemonConfig = new PickupDaemonConfig();
    }

    public PickupDaemonConfig getPickupDaemonConfig() {
        return appConfig.getPickupDaemonConfig();
    }

    public void setDefaultConfig(DefaultApplicationConfig defaultAppConfiguration) {
        appConfig = defaultAppConfiguration;
    }

    public DaemonConfig getProcessingDaemonConfig() {
        return processingDaemonConfig;
    }

    public void setProcessingDaemonConfig(DaemonConfig processingDaemonConfig) {
        this.processingDaemonConfig = processingDaemonConfig;
    }
}
