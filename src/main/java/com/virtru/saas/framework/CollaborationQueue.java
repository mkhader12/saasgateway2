package com.virtru.saas.framework;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;



public class CollaborationQueue<E> {
    final BlockingQueue<E> collaborationQueue;

    public CollaborationQueue(int collaborationQueueSize) {
        collaborationQueue=new ArrayBlockingQueue<>(collaborationQueueSize);
    }


    public synchronized boolean contains(E requestObject) {
        return collaborationQueue.contains(requestObject);
    }

    public synchronized void add(E requestObject) {
        if (!collaborationQueue.contains(requestObject)) {
            collaborationQueue.add(requestObject);
        }
    }

    public synchronized E poll() {
        return collaborationQueue.poll();
    }
}
