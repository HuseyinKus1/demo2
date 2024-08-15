package com.fmss.userservice.model;


import com.fmss.userservice.exception.ResourceNotFoundException;
import com.fmss.userservice.model.enums.PackageType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users-packages")
public class UserPackages extends AbstractBaseModel{
    @ManyToOne
    private User user;

    @ManyToOne
    private Packages packages;

    private Instant buyingDate;
    private Instant expirationDate;
    private Integer remainingListings;
}
