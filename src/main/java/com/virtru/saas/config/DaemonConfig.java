package com.virtru.saas.config;


public class DaemonConfig {
    private long maxMemoryInMBytes;
    private int numberOfWorkerThreads;

    public long getMaxMemoryInMBytes() {
        return maxMemoryInMBytes;
    }

    public void setMaxMemoryInMBytes(int maxMemoryInMBytes) {
        this.maxMemoryInMBytes = maxMemoryInMBytes * 1024 * 1024;
    }

    public int getNumberOfWorkerThreads() {
        return numberOfWorkerThreads;
    }

    public void setNumberOfWorkerThreads(int numberOfWorkerThreads) {
        this.numberOfWorkerThreads = numberOfWorkerThreads;
    }
}
