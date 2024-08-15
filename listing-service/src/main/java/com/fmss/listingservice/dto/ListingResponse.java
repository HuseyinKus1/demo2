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
public class ListingResponse {

    private String id;

    private Long userId;

    private String propertyId;

    private String title;

    private BigDecimal price;

    private String info;

    private ListingStatus listingStatus;

    private String listingType;

}
