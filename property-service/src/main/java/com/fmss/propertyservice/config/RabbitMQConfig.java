package com.fmss.propertyservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.exchange.listing}")
    private String listingExchangeName;

    @Value("${rabbitmq.queue.property-updates}")
    private String propertyUpdatesQueueName;

    @Value("${rabbitmq.routing-key.property-update}")
    private String propertyUpdateRoutingKey;

    @Bean
    public Queue propertyUpdatesQueue() {
        return new Queue(propertyUpdatesQueueName, true);
    }

    @Bean
    public TopicExchange listingExchange() {
        return new TopicExchange(listingExchangeName);
    }

    @Bean
    public Binding propertyUpdatesBinding(Queue propertyUpdatesQueue, TopicExchange listingExchange) {
        return BindingBuilder.bind(propertyUpdatesQueue).to(listingExchange).with(propertyUpdateRoutingKey);
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