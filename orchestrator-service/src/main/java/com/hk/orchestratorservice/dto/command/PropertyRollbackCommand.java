package com.hk.orchestratorservice.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRollbackCommand {
    private String sagaId;
    private String listingId;
    private String propertyId;
}