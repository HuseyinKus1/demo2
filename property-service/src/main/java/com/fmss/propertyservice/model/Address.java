package com.fmss.propertyservice.model;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    private String id;

    private String city;

    private String district;


}
