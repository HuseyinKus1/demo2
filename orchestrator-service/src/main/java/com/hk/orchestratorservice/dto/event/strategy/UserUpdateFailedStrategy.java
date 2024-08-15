package com.hk.orchestratorservice.dto.event.strategy;

import com.hk.orchestratorservice.dto.command.ListingRollbackCommand;
import com.hk.orchestratorservice.dto.command.PropertyRollbackCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserUpdateFailedStrategy implements SagaEventStrategy {
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.listing}")
    private String exchangeName;
    @Value("${rabbitmq.routing-key.property-rollback}")
    private String propertyRollbackRoutingKey;
    @Value("${rabbitmq.routing-key.listing-update}")
    private String listingUpdateRoutingKey;
    @Override
    public void handle(SagaEvent event) {
        PropertyRollbackCommand propertyCommand = new PropertyRollbackCommand(event.getSagaId(), event.getListingId(), event.getPropertyId());
        rabbitTemplate.convertAndSend(exchangeName, propertyRollbackRoutingKey, propertyCommand);
        log.info("Sent property rollback command for saga: {}", event.getSagaId());

        ListingRollbackCommand listingCommand = new ListingRollbackCommand(event.getSagaId(), event.getListingId());
        rabbitTemplate.convertAndSend(exchangeName, listingUpdateRoutingKey, listingCommand);
        log.info("Sent listing rollback command for saga: {}", event.getSagaId());
    }
}