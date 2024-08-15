package com.fmss.listingservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "property-service")
public interface PropertyClient {
    @PostMapping("/api/v1/properties/is-eligible")
    ResponseEntity<Boolean> isPropertyEligible(@RequestParam Long userId, @RequestParam String propertyId);
}
