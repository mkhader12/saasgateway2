package com.virtru.saas.daemon.pickup;


import java.io.IOException;

import com.virtru.saas.app.ApplicationContext;
import com.virtru.saas.framework.FilePoller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PickupDaemonFilePoller extends FilePoller {
    private static final Logger LOG = LogManager.getLogger(PickupDaemonFilePoller.class);


    public PickupDaemonFilePoller(ApplicationContext applicationContext) throws IOException {
        super(applicationContext);
    }

    @Override
    public String getFilePath() {
        ApplicationContext appCtx = getAppCtx();
        return appCtx.getPickupDaemonConfig().getMessageFilesFolderPath();
    }

}