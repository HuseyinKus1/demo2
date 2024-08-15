package com.fmss.userservice.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.exchange.listing}")
    private String listingExchange;

    @Value("${rabbitmq.queue.user-events}")
    private String userEventsQueue;

    @Value("${rabbitmq.routing-key.user-update}")
    private String userUpdateRoutingKey;

    @Bean
    public Queue userEventsQueue() {
        return new Queue(userEventsQueue, true);
    }

    @Bean
    public TopicExchange listingExchange() {
        return new TopicExchange(listingExchange);
    }

    @Bean
    public Binding userEventsBinding(Queue userEventsQueue, TopicExchange listingExchange) {
        return BindingBuilder.bind(userEventsQueue).to(listingExchange).with(userUpdateRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}