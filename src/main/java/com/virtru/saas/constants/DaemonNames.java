package com.virtru.saas.constants;


public enum DaemonNames {
    PICKUP_DAEMON("Pickup Daemon"),
    PROCESSING_DAEMON("Processing Daemon"),
    DELIVERY_DAEMON("Delivery Daemon");

    private final String name;

    DaemonNames(String s) {
        name = s;
    }
}
