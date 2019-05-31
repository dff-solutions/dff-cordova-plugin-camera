package com.dff.cordova.plugin.camera.exceptions;

import com.dff.cordova.plugin.camera.configurations.ActionHandlerThread;
import com.dff.cordova.plugin.camera.log.Log;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import static junit.framework.TestCase.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UnexpectedExceptionHandlerUnitTest {
    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Mock
    private NumberFormatException mNumberFormatException;

    @Mock
    Log mLog;

    @InjectMocks
    private UnexpectedExceptionHandler mUnexpectedExceptionHandler;

    @Test
    public void testUnexpectedException() throws InterruptedException {
        UnexpectedExceptionHandler spy = Mockito.spy(mUnexpectedExceptionHandler);
        Thread thread = new Thread(() -> {
            throw mNumberFormatException;
        });

        thread.setUncaughtExceptionHandler(spy);
        assertSame(spy, thread.getUncaughtExceptionHandler());
        thread.start();
        thread.join();
      
        verify(spy, times(1)).uncaughtException(thread, mNumberFormatException);
        verify(mLog, times(1)).e(anyString(), anyString(), eq(mNumberFormatException));

        assertEquals(Thread.State.TERMINATED, thread.getState());
    }

    @Test
    public void testActionHandlerThread() {
        ActionHandlerThread thread = new ActionHandlerThread(mUnexpectedExceptionHandler);
        assertEquals(mUnexpectedExceptionHandler, thread.getUncaughtExceptionHandler());
    }
}
