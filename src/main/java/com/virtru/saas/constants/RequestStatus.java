package com.virtru.saas.constants;


public enum RequestStatus {
    FAILED_IN_PICKUP("Failed In Pickup"),
    BOUNCED_AFTER_PICKUP("Bounced After Pickup"),
    READY_FOR_PROCESSING("Ready For Processing"),
    PROCESS_COMPLETED("Process Completed"),
    READY_FOR_DELIVERY("Ready For Delivery"),
    FAILED_IN_PROCESSING("Failed In Processing"),
    BOUNCED("Bounced"),
    RETRY("Retry");

    private final String name;

    RequestStatus(String queueName) {
        this.name = queueName;
    }
}
