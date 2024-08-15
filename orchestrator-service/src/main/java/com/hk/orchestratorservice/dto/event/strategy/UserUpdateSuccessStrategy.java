package com.hk.orchestratorservice.dto.event.strategy;

import com.hk.orchestratorservice.dto.command.ListingStatusUpdateCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
public class UserUpdateSuccessStrategy implements SagaEventStrategy {
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.listing}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key.listing-status-update}")
    private String listingStatusUpdateRoutingKey;
    @Override
    public void handle(SagaEvent event) {
        ListingStatusUpdateCommand listingStatusUpdateCommand = new ListingStatusUpdateCommand(event.getListingId(), "ACTIVE");
        rabbitTemplate.convertAndSend(exchangeName,listingStatusUpdateRoutingKey,listingStatusUpdateCommand);
        log.info("Saga completed successfully: {}", event.getSagaId());
    }
}