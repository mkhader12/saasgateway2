package com.virtru.saas.config;


public class DefaultApplicationConfig  extends  AppConfig {
    private static long timeBetweenRequestInMsec=1000L;
    private static int numberOfRetryAttempts=1;
    private static int numberOfPollerInstance=2;
    private static long timeBetweenPollingRequestInMsec=1000L;
    private static int numberOfProcessWorkers=10;
    private static int collaborationQueueSize=100;

    public static long getTimeBetweenRequestInMsec() {
        return timeBetweenRequestInMsec;
    }

    public static void setTimeBetweenRequestInMsec(long timeBetweenRequestInMsec) {
        DefaultApplicationConfig.timeBetweenRequestInMsec = timeBetweenRequestInMsec;
    }

    public static int getNumberOfRetryAttempts() {
        return numberOfRetryAttempts;
    }

    public static void setNumberOfRetryAttempts(int numberOfRetryAttempts) {
        DefaultApplicationConfig.numberOfRetryAttempts = numberOfRetryAttempts;
    }

    public static int getNumberOfPollerInstance() {
        return numberOfPollerInstance;
    }

    public static void setNumberOfPollerInstance(int numberOfPollerInstance) {
        DefaultApplicationConfig.numberOfPollerInstance = numberOfPollerInstance;
    }

    public static long getTimeBetweenPollingRequestInMsec() {
        return timeBetweenPollingRequestInMsec;
    }

    public static void setTimeBetweenPollingRequestInMsec(long timeBetweenPollingRequestInMsec) {
        DefaultApplicationConfig.timeBetweenPollingRequestInMsec = timeBetweenPollingRequestInMsec;
    }

    public static int getNumberOfProcessWorkers() {
        return numberOfProcessWorkers;
    }

    public static void setNumberOfProcessWorkers(int numberOfProcessWorkers) {
        DefaultApplicationConfig.numberOfProcessWorkers = numberOfProcessWorkers;
    }

    public static int getCollaborationQueueSize() {
        return collaborationQueueSize;
    }

    public static void setCollaborationQueueSize(int collaborationQueueSize) {
        DefaultApplicationConfig.collaborationQueueSize = collaborationQueueSize;
    }

    public PickupDaemonConfig getPickupDaemonConfig() {
        PickupDaemonConfig pickupDaemonConfig = new PickupDaemonConfig();
        pickupDaemonConfig.setMessageFilesFolderPath(System.getProperty("user.home"));
        pickupDaemonConfig.setNumberOfWorkerThreads(5);
        pickupDaemonConfig.setMaxMemoryInMBytes(50);

        return pickupDaemonConfig;
    }
}
