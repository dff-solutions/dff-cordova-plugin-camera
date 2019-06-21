package com.dff.cordova.plugin.camera.log;

import android.support.annotation.NonNull;

import com.dff.cordova.plugin.camera.helpers.PackageManagerHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;

/**
 * Log class.
 *
 * @see <a href="https://www.slf4j.org/api/org/slf4j/Logger.html"
 *      >https://www.slf4j.org/api/org/slf4j/Logger.html</a>
 * @see <a href="https://www.slf4j.org/apidocs/org/slf4j/LoggerFactory.html"
 *      >https://www.slf4j.org/apidocs/org/slf4j/LoggerFactory.html</a>
 */
@Singleton
public class Log {
    private static final String TAG = "Log";

    private final String logFilePrimary;
    private final String logFileHistoryPattern;

    private CallbackContextAppender callbackContextAppender;
    private PackageManagerHelper packageManagerHelper;

    @Inject
    public Log(
        CallbackContextAppender callbackContextAppender,
        PackageManagerHelper packageManagerHelper
    ) {
        this.callbackContextAppender = callbackContextAppender;
        this.packageManagerHelper = packageManagerHelper;
        String logsPath = this.packageManagerHelper.getDataDir();

        logFilePrimary = logsPath + "/plugin-camera.log";
        logFileHistoryPattern = logsPath + "/plugin-camera.%i.log";

        configureLogbackDirectly();

        d(TAG, "logsPath: " + logsPath);
    }

    private void configureLogbackDirectly() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.reset();

        ch.qos.logback.classic.Logger root =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        if (packageManagerHelper.isDebuggable()) {
            root.setLevel(Level.DEBUG);
        } else {
            root.setLevel(Level.INFO);
        }

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();

        rollingFileAppender.setContext(lc);
        rollingFileAppender.setFile(logFilePrimary);
        rollingFileAppender.setEncoder(createEncoder(lc));
        rollingFileAppender.setTriggeringPolicy(getTriggeringPolicy());
        rollingFileAppender.setRollingPolicy(getRollingPolicy(lc, rollingFileAppender));
        rollingFileAppender.start();

        AsyncAppender asyncAppender = new AsyncAppender();
        asyncAppender.setContext(lc);
        asyncAppender.addAppender(rollingFileAppender);
        asyncAppender.start();
        root.addAppender(asyncAppender);

        PatternLayoutEncoder logcatEncoder = new PatternLayoutEncoder();
        logcatEncoder.setContext(lc);
        logcatEncoder.setPattern("%msg");
        logcatEncoder.start();

        LogcatAppender logcatAppender = new LogcatAppender();
        logcatAppender.setContext(lc);
        logcatAppender.setEncoder(logcatEncoder);
        logcatAppender.start();
        root.addAppender(logcatAppender);

        callbackContextAppender.setContext(lc);
        callbackContextAppender.start();
        root.addAppender(callbackContextAppender);

        d(TAG, "configureLogbackDirectly: finished");
    }

    @NonNull
    private TriggeringPolicy<ILoggingEvent> getTriggeringPolicy() {
        return new SizeBasedTriggeringPolicy<>("1MB");
    }

    @NonNull
    private FixedWindowRollingPolicy getRollingPolicy(
        LoggerContext lc,
        RollingFileAppender<ILoggingEvent> rollingFileAppender
    ) {
        FixedWindowRollingPolicy rollingPolicy = new FixedWindowRollingPolicy();
        rollingPolicy.setContext(lc);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.setFileNamePattern(logFileHistoryPattern);
        rollingPolicy.setMinIndex(1);
        rollingPolicy.setMaxIndex(1);
        rollingPolicy.start();
        return rollingPolicy;
    }

    @NonNull
    private Encoder<ILoggingEvent> createEncoder(LoggerContext lc) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(lc);
        String logMessagePattern = "%d{ISO8601} [%thread] %-5level %logger - %msg%n";
        encoder.setPattern(logMessagePattern);
        encoder.start();
        return encoder;
    }

    private boolean needLog() {
        return true;
    }

    public void d(String tag, String msg) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).debug(msg);
        }
    }

    public void d(String tag, String msg, Throwable throwable) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).debug(msg, throwable);
        }
    }

    public void e(String tag, String msg) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).error(msg);
        }
    }

    public void e(String tag, String msg, Throwable throwable) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).error(msg, throwable);
        }
    }

    public void i(String tag, String msg) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).info(msg);
        }
    }

    public void i(String tag, String msg, Throwable throwable) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).info(msg, throwable);
        }
    }

    public void v(String tag, String msg) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).trace(msg);
        }
    }

    public void v(String tag, String msg, Throwable throwable) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).trace(msg, throwable);
        }
    }

    public void w(String tag, String msg) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).warn(msg);
        }
    }

    public void w(String tag, String msg, Throwable throwable) {
        if (needLog()) {
            LoggerFactory.getLogger(tag).warn(msg, throwable);
        }
    }
}
