package com.fmss.listingservice.feign;

import com.fmss.listingservice.configuration.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", configuration = FeignClientConfig.class)
public interface UserClient {
    @PostMapping("/api/v1/users/is-eligible")
    ResponseEntity<Boolean> isUserEligible(@RequestParam String userId, @RequestParam String listingType);


}
