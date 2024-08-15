package com.hk.orchestratorservice.dto.event;

import com.hk.orchestratorservice.dto.event.strategy.SagaEventStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SagaEventStrategyFactory {
    private final Map<String, SagaEventStrategy> strategies;

    public SagaEventStrategyFactory(List<SagaEventStrategy> strategyList) {
        strategies = strategyList.stream()
            .collect(Collectors.toMap(
                strategy -> strategy.getClass().getSimpleName().replace("Strategy", "").toUpperCase(),
                Function.identity()
            ));
    }

    public SagaEventStrategy getStrategy(String eventType) {
        SagaEventStrategy strategy = strategies.get(eventType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown event type: " + eventType);
        }
        return strategy;
    }
}