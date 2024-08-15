package com.fmss.listingservice.dto;

import com.fmss.listingservice.model.ListingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateListingRequest {
    private String id;
    private String title;
    private String info;
    private BigDecimal price;
}
