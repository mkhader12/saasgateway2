package com.virtru.saas.framework;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.virtru.saas.app.ApplicationContext;
import com.virtru.saas.config.DefaultApplicationConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
    This is an Abstract class which can be inherited by concrete class
    to implement the daemon
 */
public abstract class AbstractDaemon implements IdComponent {
    private static final Logger LOG = LogManager.getLogger(AbstractDaemon.class);
    private CollaborationQueue collaborationQueue;

    protected ApplicationContext getAppCtx() {
        return appCtx;
    }

    private final ApplicationContext appCtx;

    // Daemon supporting components
    private Poller poller;
    private DecisionMaker decisionMaker;
    private RejectHandler rejectionHandler;
    private FailureHandler failureHandler;
    private Worker worker;

    // Daemon threads
    private List<Thread> pollerThreads = new ArrayList<>();

    private List<Thread> workerThreads= Collections.synchronizedList(new ArrayList<>());

    //
    // Initialize the necessary support components for the daemon to process requests
    //
    public AbstractDaemon(ApplicationContext applicationContext) {
        this.appCtx = applicationContext;
        this.poller = this.getPoller();
        this.decisionMaker = this.getDecisionMaker();
        this.failureHandler = this.getFailureHandler();
        this.worker = this.getProcessWorker();
        collaborationQueue = new CollaborationQueue(getCollaborationQueueSize());
    }

    //
    // Daemon component supply methods
    //
    // The poller is a required component
    // It must be supplied by the implementing class
    protected abstract Poller getPoller();

    //
    // The Process worker is a required component
    // It must be supplied by the implementing class
    protected abstract Worker getProcessWorker();

    //
    // The Failure handler is an optional component which handles the failure
    //   of the processing work
    // The implementing class may choose to override and supply one
    protected FailureHandler getFailureHandler() {
        LOG.warn(this.getName()+" doesn't have a failure handler");
        return null;
    }

    //
    // The Decision maker is an optional component
    //  it helps whether to process the request based certain criteria
    //      such as type of request or size of request etc.
    // The implementing class may choose to override and supply one
    protected DecisionMaker getDecisionMaker() {
        LOG.warn(this.getName()+" doesn't have a decision maker -- All requests will be processed");
        return null;
    }

    //
    // This is a main daemon method which starts all the daemon process
    //
    public final void start() {

        // Start the Poller Thread
        //    based on the number of poller instance
        for (int i=0; i < getNumberOfPollerInstance(); i++) {
            Thread pollerThread = getPollerThread();
            pollerThreads.add(pollerThread);
            LOG.info("Starting Poller thread"+ pollerThread.getName());
            pollerThread.start();
        }


        for (int i=0; i < getNumberOfProcessWorkers(); i++) {
            Thread workerThread = getWorkerThread();
            workerThreads.add(workerThread);
            LOG.info("Starting Poller thread"+ workerThread.getName());
            workerThread.start();
        }

        // Keep waiting in the event loop
        Thread eventLoopThread = getEventLoopThread();
        eventLoopThread.start();
    }

    //
    // Create event loop thread for the daemon
    //
    private Thread getEventLoopThread() {
        Runnable runnable = () -> {
                eventLoop();
        };
        return new Thread(runnable);
    }

    //
    // Create Poller Thread instance
    //
    private  Thread getPollerThread() {
        Runnable runnable = () -> {
                LOG.info("Poller "+ Thread.currentThread().getName() +" starts polling");
                poller.startPolling(collaborationQueue, getTimeBetweenPollingRequestInMsec());
        };

        return new Thread(runnable);
    }

    //
    // Create Worker Thread instance
    //
    private final Thread getWorkerThread() {
        Runnable runnable = () ->  {
                performWork();
        };

        return new Thread(runnable);

    }

    //
    // The worker thread's method that keeps looking for work
    //
    protected final void performWork() {
        while(true) {
           // LOG.info("Worker "+ Thread.currentThread().getName() +" waiting for request");
            Object newRequest = getNextItemFromRequestQueue();
            if (newRequest != null) {
                // Got new request
                //
                // Process the request
                LOG.info(worker.getName() + " got the new request = "+ newRequest);

                // This method handles the request
                processRequest(newRequest);

                //LOG.info(worker.getName() + " finished processing request = "+ newRequest);

            }
            else
            {
                // No work
                // When there is no request wait for elapsed
                // number of milli seconds
                try {
                    Thread.sleep(getTimeBetweenRequestInMsec());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    //
    // This is the eventloop where the daemon process
    //
    private void eventLoop() {
        // Check to ensure the system is not shutting down
        LOG.info(getName() + " entering eventloop");
        while (!isSystemShuttingDown()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    protected final void processRequest(Object requestObject) {
        //
        // Before processing the request
        // Check to see if the request can be performed

        //
        // By default all requests have to be processed
        boolean continueProcess = true;

        //
        // First it will be determined by the decision maker
        //    if the decisionmaker exists for the daemon
        if (decisionMaker != null)
        {
            continueProcess = decisionMaker.canPerformWork(requestObject);
            if (!continueProcess) {
                LOG.info(getName() + " request is handled by rejection handler");
                // If the request can not processed by the daemon
                // It should be handled by the rejection handler
                rejectionHandler.rejectRequest(requestObject);
            }
        }

        //
        // Perform the actual business function here
        if (continueProcess) {
            //
            // Process only if it is determined above as to process further
            int retry = 0;
            while (retry < getNumberOfRetryAttempts()) {
                try {
                    worker.performWork(requestObject);
                    break;
                }
                catch (Exception e) {
                    retry++;
                    LOG.error(e);
                }

            }
        }
    }

    //
    // Collaboration queue related methods
    //

    protected synchronized void addRequestToQueue(Object requestObject) {
        if (!collaborationQueue.contains(requestObject)) {
            collaborationQueue.add(requestObject);
        }
    }


    private synchronized Object getNextItemFromRequestQueue() {
        return collaborationQueue.poll();
    }

    // Config related methods

    //
    // Number of poller instance config
    private int getNumberOfPollerInstance() {
        return DefaultApplicationConfig.getNumberOfPollerInstance();
    }

    private int getNumberOfRetryAttempts() {
        return DefaultApplicationConfig.getNumberOfRetryAttempts();
    }

    private boolean isSystemShuttingDown() {
        return false;
    }

    private long getTimeBetweenRequestInMsec() {
        return DefaultApplicationConfig.getTimeBetweenRequestInMsec();
    }

    private long getTimeBetweenPollingRequestInMsec() {
        return DefaultApplicationConfig.getTimeBetweenPollingRequestInMsec();
    }


    private int getNumberOfProcessWorkers() {
        return DefaultApplicationConfig.getNumberOfProcessWorkers();
    }

    private int getCollaborationQueueSize() {
        return DefaultApplicationConfig.getCollaborationQueueSize();
    }

}
