package com.virtru.saas.config;


public class AppConfig {
    protected PickupDaemonConfig pickupDaemonConfig;

    public AppConfig() {

        pickupDaemonConfig = getPickupDaemonConfig();

    }

    public PickupDaemonConfig getPickupDaemonConfig() {
        return pickupDaemonConfig;
    }
}
