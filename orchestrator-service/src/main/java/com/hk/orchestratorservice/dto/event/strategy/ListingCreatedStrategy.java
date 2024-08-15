package com.hk.orchestratorservice.dto.event.strategy;

import com.hk.orchestratorservice.dto.command.PropertyUpdateCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ListingCreatedStrategy implements SagaEventStrategy {
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.listing}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key.property-update}")
    private String propertyUpdateRoutingKey;

    @Override
    public void handle(SagaEvent event) {
        PropertyUpdateCommand command = PropertyUpdateCommand.builder()
                .sagaId(event.getSagaId())
                .propertyId(event.getPropertyId())
                .userId(event.getUserId())
                .listingId(event.getListingId())
                .listingType(event.getListingType())
                .build();
        rabbitTemplate.convertAndSend(exchangeName, propertyUpdateRoutingKey, command);
        log.info("Sent property update command for saga: {}", event.getSagaId());
    }
}