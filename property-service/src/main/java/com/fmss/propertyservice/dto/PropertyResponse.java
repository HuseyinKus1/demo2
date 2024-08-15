package com.fmss.propertyservice.dto;

import com.fmss.propertyservice.model.Address;
import com.fmss.propertyservice.model.Type;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {

    private String id;
    private Type type;
    private Long userId;
    private String listingId;
    private Address address;
    private String locationDescript;
    private String imageUrl;
    private int age;
    private int floor;
    private double area;
    private boolean isListed;
}
