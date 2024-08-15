package com.fmss.userservice.mapper;

import com.fmss.userservice.dto.response.UserPackagesResponse;
import com.fmss.userservice.model.Packages;
import com.fmss.userservice.model.User;
import com.fmss.userservice.model.UserPackages;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class UserPackagesMapper {
   public UserPackages toUserPackages(User user, Packages packages){
       Instant now = Instant.now();

       UserPackages.UserPackagesBuilder userPackages = UserPackages.builder();
       userPackages.user(user);
       userPackages.remainingListings(packages.getRightsToListing());
       userPackages.packages(packages);
       userPackages.buyingDate(now);
       userPackages.expirationDate((now.plus(Duration.ofDays(packages.getValidityDays()))));
       return userPackages.build();
   }
    public UserPackagesResponse toUserPackagesResponse(UserPackages userPackages){

        UserPackagesResponse.UserPackagesResponseBuilder userPackagesResponse = UserPackagesResponse.builder();
        userPackagesResponse.buyingDate(userPackages.getBuyingDate());
        userPackagesResponse.expirationDate(userPackages.getExpirationDate());
        userPackagesResponse.packages(userPackages.getPackages());
        userPackagesResponse.remainingListings(userPackages.getRemainingListings());
        return userPackagesResponse.build();
    }
}
