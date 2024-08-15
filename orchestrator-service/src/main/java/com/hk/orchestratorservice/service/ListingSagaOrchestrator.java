package com.hk.orchestratorservice.service;

import com.hk.orchestratorservice.dto.event.strategy.SagaEvent;
import com.hk.orchestratorservice.dto.event.strategy.SagaEventStrategy;
import com.hk.orchestratorservice.dto.event.SagaEventStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingSagaOrchestrator {
    private final SagaEventStrategyFactory strategyFactory;
    @RabbitListener(queues = "${rabbitmq.queue.saga-events}")
    public void handleSagaEvent(SagaEvent event) {
        log.info("Orchestrator received event: {}", event);
        try {
            SagaEventStrategy strategy = strategyFactory.getStrategy(event.getEventType());
            strategy.handle(event);
        } catch (IllegalArgumentException e) {
            log.warn("Unknown event type: {}", event.getEventType());
        }
    }
}