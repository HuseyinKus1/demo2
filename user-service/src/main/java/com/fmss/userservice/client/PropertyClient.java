package com.fmss.userservice.client;

import com.fmss.userservice.dto.response.PropertyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "property-service")
public interface PropertyClient {
    @GetMapping("api/v1/properties/str")
    ResponseEntity<String> getPropertyById(@RequestParam("id") String id);

    @GetMapping("api/v1/properties/user-properties")
    ResponseEntity<Page<PropertyResponse>> getUserProperties(@RequestParam("userId") Long userId, @RequestParam("page") int page, @RequestParam("size") int size);

}
