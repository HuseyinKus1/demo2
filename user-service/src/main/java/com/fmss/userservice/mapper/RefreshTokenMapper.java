package com.fmss.userservice.mapper;



import com.fmss.userservice.model.RefreshToken;
import com.fmss.userservice.model.enums.RefreshTokenStatus;
import com.fmss.userservice.model.User;
import org.springframework.stereotype.Component;

import java.time.Instant;

//@Mapper(implementationName = "RefreshTokenMapperImpl", componentModel = "spring")
@Component
public class RefreshTokenMapper {
    public RefreshToken toRefreshToken(String refreshToken, User user, Instant expirationDate, RefreshTokenStatus refreshTokenStatus) {
        if ( refreshToken == null && user == null && expirationDate == null && refreshTokenStatus == null ) {
            return null;
        }

        RefreshToken.RefreshTokenBuilder refreshToken1 = RefreshToken.builder();

        refreshToken1.token( refreshToken );
        refreshToken1.user( user );
        refreshToken1.expirationDate( expirationDate );
        refreshToken1.refreshTokenStatus( refreshTokenStatus );

        return refreshToken1.build();
    }}
