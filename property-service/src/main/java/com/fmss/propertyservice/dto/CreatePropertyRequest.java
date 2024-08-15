package com.fmss.propertyservice.dto;

import com.fmss.propertyservice.model.Address;
import com.fmss.propertyservice.model.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePropertyRequest {

    private Long userId;

    @NotBlank(message = "Building Type must be specified")
    private Type type;

    @NotBlank(message = "Address is required")
    private AddressDto address;

    private String locationDescript;

    private String imageUrl;

    private int age;

    private int floor;

    private double area;

}
