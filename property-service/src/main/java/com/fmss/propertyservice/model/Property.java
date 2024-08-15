package com.fmss.propertyservice.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "properties")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private String id;

    @Nonnull
    private Type type;

    private Long userId;

    private String listingId;

    private Address address;



    @Nullable
    private String locationDescript;

    private String imageUrl;

    private int age;

    private int floor;

    private double area;
    @Builder.Default
    private boolean isListed = false;

    @Override
    public String toString() {
        return "Building{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", listing=" + listingId +
                ", address=" + address +
                ", locationDescript='" + locationDescript + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", age=" + age +
                ", floor=" + floor +
                ", area=" + area +
                ", isListed=" + isListed +
                '}';
    }
}
