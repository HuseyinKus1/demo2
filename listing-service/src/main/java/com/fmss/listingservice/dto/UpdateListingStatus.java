package com.fmss.listingservice.dto;

import com.fmss.listingservice.model.ListingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateListingStatus {
    private String id;
    private ListingStatus listingStatus;
}
