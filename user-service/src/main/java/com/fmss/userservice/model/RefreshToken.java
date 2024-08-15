package com.fmss.userservice.model;


import com.fmss.userservice.model.enums.RefreshTokenStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "refreshtokens")
public class RefreshToken extends AbstractBaseModel{
    @Column(nullable = false,unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Instant expirationDate;

    @Enumerated(EnumType.STRING)
    private RefreshTokenStatus refreshTokenStatus;
}
