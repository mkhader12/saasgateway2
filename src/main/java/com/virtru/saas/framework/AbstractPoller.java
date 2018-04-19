package com.virtru.saas.framework;


import java.util.Collection;

import com.virtru.saas.app.ApplicationContext;


public abstract class AbstractPoller<E> implements Poller {
    private CollaborationQueue<E> internalQueue;

    protected ApplicationContext getAppCtx() {
        return appCtx;
    }

    private final ApplicationContext appCtx;

    public AbstractPoller(ApplicationContext applicationContext) {
        this.appCtx = applicationContext;

    }

    protected final E getFromQueue()  {
        return internalQueue.poll();
    }

    @Override
    public void startPolling(CollaborationQueue queue, long elapseTimeBetweenPollingInMsec) {
        this.internalQueue = queue;
        while(true) {
            E incomingRequestObj = getPollingRequestObject();
            if (incomingRequestObj != null) {
                if (incomingRequestObj instanceof Collection) {
                    Collection<E> c = (Collection<E>) incomingRequestObj;
                    for (Object anObject : c.toArray()) {
                        addToInternalQueue((E) anObject);
                    }
                }
                else {
                    addToInternalQueue(incomingRequestObj);
                }
            }
            try {
                Thread.sleep(elapseTimeBetweenPollingInMsec);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void addToInternalQueue(E requestObj) {
        internalQueue.add(requestObj);
    }
    @Override
    public String getName() {
        return getClass().getName();
    }
    protected abstract E getPollingRequestObject();
}