package com.hk.orchestratorservice.dto.event.strategy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SagaEvent {
    private String sagaId;
    private String eventType;
    private String listingId;
    private String propertyId;
    private String userId;
    private String listingType;
}