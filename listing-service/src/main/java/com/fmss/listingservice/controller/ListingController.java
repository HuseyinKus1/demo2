package com.fmss.listingservice.controller;


import com.fmss.listingservice.dto.*;
import com.fmss.listingservice.model.ListingStatus;
import com.fmss.listingservice.service.ListingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/listings")
@RequiredArgsConstructor
@Slf4j
public class ListingController {
    private final ListingService listingService;

    @PostMapping()
    public ResponseEntity<ApiResponse<ListingResponse>> create(@RequestBody CreateListingRequest createListingRequest) {
        return ResponseEntity.ok(new ApiResponse<>("success",listingService.createListing1(createListingRequest),"listed"));
    }

    @PutMapping()
    public ResponseEntity<ApiResponse<ListingResponse>> update(@RequestBody UpdateListingRequest updateListingRequest){

        return ResponseEntity.ok(new ApiResponse<>("success",listingService.update(updateListingRequest),"listing updated successfully"));

    }

    @PatchMapping()
    public ResponseEntity<ApiResponse<ListingResponse>> updateStatus(@RequestBody UpdateListingStatus updateListingStatus){
        return ResponseEntity.ok(new ApiResponse<>("success",listingService.updateStatus(updateListingStatus),"listing status updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteById(@PathVariable String id){
        return ResponseEntity.ok(new ApiResponse<>("success",listingService.delete(id),"listing deleted successfully"));
    }
    @GetMapping("/all-active-listings")
    public ResponseEntity<ApiResponse<Page<ListingResponse>>> getAllActiveListings(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                                   @RequestParam(name = "size", defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new ApiResponse<>("success",listingService.getAllActive(pageable),"all active listings"));
    }
    @GetMapping("/all-listings-by-user")
    public ResponseEntity<ApiResponse<Page<ListingResponse>>> getUsersListings(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                                   @RequestParam(name = "size", defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new ApiResponse<>("success",listingService.getAllByUser(pageable),"all active listings"));
    }

    @GetMapping("/all-listings-by-user-and-status")
    public ResponseEntity<ApiResponse<Page<ListingResponse>>> getUsersActiveListings(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                                     @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam ListingStatus listingStatus){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new ApiResponse<>("success",listingService.getAllByUserIdAndStatus(pageable, listingStatus),"all listings of individual by status"));
    }




}
