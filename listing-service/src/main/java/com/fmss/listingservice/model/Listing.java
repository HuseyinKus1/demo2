package com.fmss.listingservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "listings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private Long userId;
    private String propertyId;
    private String title;
    private BigDecimal price;
    private String info;
    private ListingStatus listingStatus;
    private String listingType;

}
