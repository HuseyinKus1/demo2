package com.fmss.api_gateway.config.logger;

import lombok.Getter;
import org.slf4j.Marker;

import java.util.List;

public class MyLoggingEvent{
    @Getter
    private final String loggerName;
    @Getter
    private final List<Marker> markers;
    @Getter
    private final Level level;
    @Getter
    private final String message;
    private final long timestamp;
    @Getter
    private final Throwable throwable;
    @Getter
    private final String threadName;
    @Getter
    private final Object[] args;
    public MyLoggingEvent(String loggerName, Level level, String message, Throwable throwable, Object... args) {
        this.loggerName = loggerName;
        this.markers = null; //
        this.level = level;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.throwable = throwable;
        this.threadName = Thread.currentThread().getName();
        this.args = args;
    }

    public long getTimeStamp() {
        return timestamp;
    }

}
