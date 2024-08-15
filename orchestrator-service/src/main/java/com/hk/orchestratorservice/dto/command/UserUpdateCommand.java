package com.hk.orchestratorservice.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateCommand {
    private String sagaId;
    private String userId;
    private String listingType;
    private String listingId;
    private String propertyId;

}