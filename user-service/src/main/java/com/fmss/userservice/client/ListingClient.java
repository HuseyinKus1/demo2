package com.fmss.userservice.client;

import com.fmss.userservice.dto.response.BuildingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "listing-service")
public interface ListingClient {
    @GetMapping("api/v1/buildings/str")
    ResponseEntity<String> getBuildingById(@RequestParam("id") String id);

    @GetMapping("api/v1/buildings/get-by-user")
    ResponseEntity<BuildingResponse> getUserBuildings(@RequestParam("userId") Long userId);

}
