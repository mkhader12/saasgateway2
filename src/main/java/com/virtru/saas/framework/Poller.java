package com.virtru.saas.framework;


public interface Poller extends IdComponent {
   // Object getRequestObject();

    void startPolling(CollaborationQueue collaborationQueue, long timeBetweenPollingRequestInMsec);
}
