package com.hk.orchestratorservice.dto.event.strategy;

import com.hk.orchestratorservice.dto.command.UserUpdateCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PropertyUpdateSuccessStrategy implements SagaEventStrategy {
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.listing}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key.user-update}")
    private String userUpdateRoutingKey;
    @Override
    public void handle(SagaEvent event) {
        UserUpdateCommand command = new UserUpdateCommand(event.getSagaId(), event.getUserId(), event.getListingType(),event.getListingId(),event.getPropertyId());
        rabbitTemplate.convertAndSend(exchangeName, userUpdateRoutingKey, command);
        log.info("Sent user update command for saga: {}", event.getSagaId());
    }
}