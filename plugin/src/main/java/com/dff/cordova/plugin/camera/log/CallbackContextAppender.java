package com.dff.cordova.plugin.camera.log;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

@Singleton
public class CallbackContextAppender extends AppenderBase<ILoggingEvent> {
    private EventBus eventBus;

    @Inject
    public CallbackContextAppender(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (!isStarted()) {
            return;
        }

        if (eventBus.hasSubscriberForEvent(ILoggingEvent.class)) {
            eventBus.post(event);
        }
    }
}
