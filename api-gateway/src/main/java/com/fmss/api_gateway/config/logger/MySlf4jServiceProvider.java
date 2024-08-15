package com.fmss.api_gateway.config.logger;




import com.fmss.api_gateway.config.logger.config.ConfigFile;
import com.fmss.api_gateway.config.logger.config.ConfigFileFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class MySlf4jServiceProvider implements SLF4JServiceProvider {
    private static final String API_VERSION = "2.0.13";
    private MyLoggerFactory loggerFactory;
    private IMarkerFactory markerFactory;
    private MDCAdapter mdcAdapter;
    private final Map<String,Appender> appenders = new HashMap<>();
    @Override
    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    @Override
    public IMarkerFactory getMarkerFactory() {
        return markerFactory;
    }

    @Override
    public MDCAdapter getMDCAdapter() {
        return mdcAdapter;
    }

    @Override
    public String getRequestedApiVersion() {
        return API_VERSION;
    }

    @Override
    public void initialize() {
        appenders.put("Rabbitmq",addRabbitMQAppender("localhost", 5672, "guest", "guest", "logs_queue"));
        ConfigFileFactory configFileFactory;
        MyMDCAdapter myMDCAdapter = new MyMDCAdapter();
        configFileFactory = new ConfigFileFactory();
        ConfigFile configFile = configFileFactory.createLoader();
        Level configuredLevel = configFile.getLoggingLevelFromClasspath();
        loggerFactory = new MyLoggerFactory(myMDCAdapter, configuredLevel,appenders);
        markerFactory = new BasicMarkerFactory();
        mdcAdapter = myMDCAdapter;
    }

    public Appender addRabbitMQAppender(String host, int port, String username, String password, String queueName) {
        RabbitMQAppender rabbitMQAppender = new RabbitMQAppender(host, port, username, password, queueName);
        rabbitMQAppender.start();
        appenders.put("Rabbitmq",rabbitMQAppender);
        return appenders.get("Rabbitmq");
    }



}