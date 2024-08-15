package com.fmss.propertyservice.mapper;

import com.fmss.propertyservice.dto.CreatePropertyRequest;
import com.fmss.propertyservice.dto.PropertyResponse;
import com.fmss.propertyservice.model.Address;
import com.fmss.propertyservice.model.Property;
import org.springframework.stereotype.Component;

@Component
public class PropertyMapper {
   public Property toProperty(CreatePropertyRequest createPropertyRequest){
        Property.PropertyBuilder property = Property.builder();

        property.address(Address.builder().city(createPropertyRequest.getAddress().getCity())
                .district(createPropertyRequest.getAddress().getDistrict()).build());
        property.locationDescript(createPropertyRequest.getLocationDescript());
        property.age(createPropertyRequest.getAge());
        property.userId(createPropertyRequest.getUserId());
        property.type((createPropertyRequest.getType()));
        property.area(createPropertyRequest.getArea());
        property.imageUrl(createPropertyRequest.getImageUrl());
        property.floor(createPropertyRequest.getFloor());
        return property.build();
    }
    public PropertyResponse toPropertyResponse(Property property){
        PropertyResponse.PropertyResponseBuilder propertyResponse = PropertyResponse.builder();
        propertyResponse.id(property.getId());
        propertyResponse.userId(property.getUserId());
        propertyResponse.address(property.getAddress());
        propertyResponse.locationDescript(property.getLocationDescript());
        propertyResponse.age(property.getAge());
        propertyResponse.type((property.getType()));
        propertyResponse.area(property.getArea());
        propertyResponse.imageUrl(property.getImageUrl());
        propertyResponse.floor(property.getFloor());
        return propertyResponse.build();
    }
}
