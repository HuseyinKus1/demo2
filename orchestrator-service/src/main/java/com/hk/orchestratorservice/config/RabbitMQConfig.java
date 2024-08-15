package com.hk.orchestratorservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.exchange.listing}")
    private String listingExchange;
    @Bean
    public TopicExchange listingExchange() {
        return new TopicExchange(listingExchange);
    }
    @Value("${rabbitmq.queue.saga-events}")
    private String sagaEventsQueueName;
    @Value("${rabbitmq.queue.listing-events}")
    private String listingEventsQueue;
    @Value("${rabbitmq.queue.user-events}")
    private String userEventsQueue;
    @Value("${rabbitmq.routing-key.user-update}")
    private String userEventsRoutingKey;
    @Value("${rabbitmq.routing-key.saga-event}")
    private String sagaEventsRoutingKey;
    @Value("${rabbitmq.routing-key.listing-update}")
    private String listingEventsRoutingKey;

    @Value("${rabbitmq.queue.listing-status-events}")
    private String listingStatusEventsQueue;
    @Value("${rabbitmq.routing-key.listing-status-update}")
    private String listingStatusEventsRoutingKey;

    @Value("${rabbitmq.queue.property-rollback-events}")
    private String propertyRollbackEventsQueue;
    @Value("${rabbitmq.routing-key.property-rollback}")
    private String propertyRollbackEventsRoutingKey;

    @Bean
    public Queue propertyRollbackEventsQueue() {
        return new Queue(propertyRollbackEventsQueue, true);
    }
    @Bean
    public Binding propertyRollbackEventsBinding(Queue propertyRollbackEventsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(propertyRollbackEventsQueue).to(exchange).with(propertyRollbackEventsRoutingKey);
    }

    @Bean
    public Queue sagaEventsQueue() {
        return new Queue(sagaEventsQueueName, true);
    }
    @Bean
    public Binding sagaEventsBinding(Queue sagaEventsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(sagaEventsQueue).to(exchange).with(sagaEventsRoutingKey);
    }
    @Bean
    public Queue listingStatusEventsQueue() {
        return new Queue(listingStatusEventsQueue, true);
    }
    @Bean
    public Binding listingStatusEventsBinding(Queue listingStatusEventsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(listingStatusEventsQueue).to(exchange).with(listingStatusEventsRoutingKey);
    }
    @Bean
    public Queue listingEventsQueue() {
        return new Queue(listingEventsQueue, true);
    }
    @Bean
    public Binding listingEventsBinding(Queue listingEventsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(listingEventsQueue).to(exchange).with(listingEventsRoutingKey);
    }
    @Bean
    public Queue userEventsQueue() {
        return new Queue(userEventsQueue, true);
    }
    @Bean
    public Binding userEventsBinding(Queue userEventsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(userEventsQueue).to(exchange).with(userEventsRoutingKey);
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}