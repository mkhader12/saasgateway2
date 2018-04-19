package com.virtru.saas.constants;


public enum SqsQueueNames {
    PROCESSING_Q("ProcessingQueue"), RETRY_Q("RetryQueue"), DELIVERY_Q("DeliveryQueue"), BOUNCE_Q("BounceQueue");

    private final String name;

    SqsQueueNames(String queueName) {
        this.name = queueName;
    }
}
