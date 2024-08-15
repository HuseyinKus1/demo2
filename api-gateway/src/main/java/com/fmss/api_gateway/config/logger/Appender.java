package com.fmss.api_gateway.config.logger;

public interface Appender {
    void append(MyLoggingEvent event);
    void start();
    void stop();
}