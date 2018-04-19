package com.virtru.saas.framework.sample;


import java.util.Calendar;

import com.virtru.saas.app.ApplicationContext;
import com.virtru.saas.framework.AbstractPoller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TimePoller extends AbstractPoller {
    private static final Logger LOG = LogManager.getLogger(TimePoller.class);

    public TimePoller(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    private synchronized Object getRequestObject() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime % 5 == 0)
        {
            LOG.info(getName() + " got new request "+ currentTime);
            return currentTime;
        }
        return null;
    }

    @Override
    protected Object getPollingRequestObject() {
        return getRequestObject();
    }

    @Override
    public String getName() {
        return getClass().getName()+ Thread.currentThread().getName();
    }
}
