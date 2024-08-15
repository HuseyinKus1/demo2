package com.fmss.propertyservice.controller;

import com.fmss.propertyservice.dto.CreatePropertyRequest;
import com.fmss.propertyservice.dto.PropertyResponse;
import com.fmss.propertyservice.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/properties")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;

    @PostMapping()
    public ResponseEntity<PropertyResponse> create(@RequestBody CreatePropertyRequest createPropertyRequest){
        return ResponseEntity.ok(propertyService.create(createPropertyRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getById(@RequestParam String id){
        return ResponseEntity.ok(propertyService.getById(id));
    }

    @GetMapping()
    public ResponseEntity<Page<PropertyResponse>> getAll(@RequestParam int page, @RequestParam int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(propertyService.getAll(pageable));
    }

    @GetMapping("/user-properties/{userId}")
    public ResponseEntity<Page<PropertyResponse>> getAllByUserId(@PathVariable Long userId,@RequestParam int page, @RequestParam int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(propertyService.getByUserId(userId, pageable));
    }

    @PostMapping("is-eligible")
    public ResponseEntity<Boolean> isPropertyEligible(@RequestParam Long userId,@RequestParam String propertyId){
        return ResponseEntity.ok(propertyService.isEligible(userId,propertyId));
    }
}
