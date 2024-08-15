package com.fmss.userservice.dto.response;

import com.fmss.userservice.model.Packages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPackagesResponse {

    private Packages packages;
    private Instant buyingDate;
    private Instant expirationDate;
    private Integer remainingListings;

}
