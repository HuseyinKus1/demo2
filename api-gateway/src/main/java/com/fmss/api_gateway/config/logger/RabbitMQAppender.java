package com.fmss.api_gateway.config.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class RabbitMQAppender implements Appender {
    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String queueName;
    private Connection connection;
    private Channel channel;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RabbitMQAppender(String host, int port, String username, String password, String queueName) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.queueName = queueName;
    }
    @Override
    public void start() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setPort(port);
            factory.setUsername(username);
            factory.setPassword(password);

            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);
        } catch (IOException | TimeoutException e) {
            System.err.println("Failed to start RabbitMQ appender: " + e.getMessage());
        }
    }

    @Override
    public void append(MyLoggingEvent event) {
        try {
            String message = objectMapper.writeValueAsString(convertToMap(event));
            channel.basicPublish("", queueName, null, message.getBytes());
        } catch (IOException e) {
            System.err.println("Error sending log event to RabbitMQ: " + e.getMessage());
        }
    }
private Map<String, Object> convertToMap(MyLoggingEvent event) {
    Map<String, Object> map = new HashMap<>();
    map.put("timestamp", event.getTimeStamp());
    map.put("level", event.getLevel().toString());
    map.put("logger", event.getLoggerName());
    map.put("thread", event.getThreadName());

    String formattedMessage = String.format(event.getMessage().replace("{}", "%s"), event.getArgs());
    map.put("message", formattedMessage);
    map.put("data", String.join(", ", Arrays.stream(event.getArgs())
            .map(Object::toString)
            .toArray(String[]::new)));

    return map;
}

    @Override
    public void stop() {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException | TimeoutException e) {
                System.err.println("Error closing RabbitMQ channel: " + e.getMessage());
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                System.err.println("Error closing RabbitMQ connection: " + e.getMessage());
            }
        }
    }
}