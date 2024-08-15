package com.fmss.userservice.model;

import com.fmss.userservice.model.enums.PackageType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "packages")
public class Packages extends AbstractBaseModel {
    @Enumerated(EnumType.STRING)
    private PackageType type;
    private Integer validityDays;
    private Integer rightsToListing;
    private BigDecimal price;
}