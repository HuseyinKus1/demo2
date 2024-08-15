package com.fmss.propertyservice.dto;

import com.fmss.propertyservice.model.Property;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {


    @NotNull
    @NotBlank(message = "City cannot be empty")
    private String city;

    private String district;
}
