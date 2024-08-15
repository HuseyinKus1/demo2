package com.fmss.propertyservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyUpdateCommand {
    private String sagaId;
    private String propertyId;
    private String listingId;
    private String userId;
    private String listingType;
}