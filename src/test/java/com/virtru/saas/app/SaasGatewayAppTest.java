package com.virtru.saas.app;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.whenNew;


@RunWith(PowerMockRunner.class)
@PrepareForTest(SaasGatewayApp.class)
public class SaasGatewayAppTest {
    ArgumentCaptor<Runnable> runnables = ArgumentCaptor.forClass(Runnable.class);

    @InjectMocks
    private SaasGatewayApp app;

    @Test
    public void verifyDaemons() throws Exception {
        ApplicationContext appCtx= new ApplicationContext();
        // create a mock for Thread.class
        Thread mock = Mockito.mock(Thread.class);

        // mock the 'new Thread', return the mock and capture the given runnable
        whenNew(Thread.class).withParameterTypes(Runnable.class)
                .withArguments(runnables.capture()).thenReturn(mock);
    }

}