package com.fmss.api_gateway.config.logger;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MyLoggerFactory implements ILoggerFactory {
    private final ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<>();
    private final MyMDCAdapter myMDCAdapter;
    private final Level loggingLevel;
    private final Map<String,Appender> appendersMap;

    public MyLoggerFactory(MyMDCAdapter myMDCAdapter, Level loggingLevel, Map<String,Appender> appendersMap) {
        this.myMDCAdapter = myMDCAdapter;
        this.loggingLevel = loggingLevel;
        this.appendersMap = appendersMap;
        }

    @Override
    public Logger getLogger(String name) {
        return loggerMap.computeIfAbsent(name, this::createLogger);
    }

    private Logger createLogger(String name) {
        return new MyLogger(loggingLevel,name, myMDCAdapter,appendersMap);
    }


}