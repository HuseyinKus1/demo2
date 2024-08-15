package com.fmss.listingservice.configuration;

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
    @Value("${rabbitmq.routing-key.saga-event}")
    private String sagaEventsRoutingKey;

    @Bean
    public Queue sagaEventsQueue() {
        return new Queue(sagaEventsQueueName, true);
    }
    @Bean
    public Binding sagaEventsBinding(Queue sagaEventsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(sagaEventsQueue).to(exchange).with(sagaEventsRoutingKey);
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