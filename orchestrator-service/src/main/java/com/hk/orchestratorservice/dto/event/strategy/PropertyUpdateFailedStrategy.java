package com.hk.orchestratorservice.dto.event.strategy;

import com.hk.orchestratorservice.dto.command.ListingRollbackCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PropertyUpdateFailedStrategy implements SagaEventStrategy {
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.listing}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key.listing-update}")
    private String listingUpdateRoutingKey;

    @Override
    public void handle(SagaEvent event) {
        ListingRollbackCommand command = new ListingRollbackCommand(event.getSagaId(), event.getListingId());
        rabbitTemplate.convertAndSend(exchangeName, listingUpdateRoutingKey, command);
        log.info("Sent listing rollback command for saga: {}", event.getSagaId());
    }
}