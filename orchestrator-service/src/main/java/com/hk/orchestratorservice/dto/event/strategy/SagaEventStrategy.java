package com.hk.orchestratorservice.dto.event.strategy;

public interface SagaEventStrategy {
    void handle(SagaEvent event);
}