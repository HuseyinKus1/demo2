package com.fmss.listingservice.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    @NonNull
    private String city;

    private String district;

}
