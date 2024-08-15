package com.fmss.listingservice.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateListingRequest {

    @NotBlank
    private String title;

    @NotNull
    @NotBlank(message = "propertyId cannot be empty")
    private String propertyId;

    private String info;

    private String listingType;
}
