package com.fmss.userservice.controller;


import com.fmss.userservice.client.ListingClient;
import com.fmss.userservice.client.PropertyClient;
import com.fmss.userservice.dto.ApiResponse;
import com.fmss.userservice.dto.response.PropertyResponse;
import com.fmss.userservice.dto.response.UserPackagesResponse;
import com.fmss.userservice.dto.request.UpdateUserRequest;
import com.fmss.userservice.dto.response.BaseUserResponse;
import com.fmss.userservice.dto.request.RegisterUserRequest;
import com.fmss.userservice.model.User;
import com.fmss.userservice.model.enums.PackageType;
import com.fmss.userservice.util.CurrentUser;
import com.fmss.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final ListingClient listingClient;
    private final PropertyClient propertyClient;
    @PostMapping()
    public ResponseEntity<User> create(RegisterUserRequest registerUserRequest){
        return ResponseEntity.ok(userService.create(registerUserRequest));
    }
    @GetMapping()
    public ResponseEntity<ApiResponse<Page<BaseUserResponse>>> getAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                      @RequestParam(name = "size", defaultValue = "10") int size) {
         Pageable pageable = PageRequest.of(page, size);
         return ResponseEntity.ok(new ApiResponse<>("success", userService.getAll(pageable),"userspage"));
     }

     @PostMapping("buy-packages")
     public ResponseEntity<String> buyPackages(@RequestParam PackageType type){
        return ResponseEntity.ok(userService.buyPackages(type));
     }
    @PostMapping("decrease-listings")
    public ResponseEntity<String> decreaseListings(PackageType type){
        return ResponseEntity.ok(userService.decreaseListings(type));
    }

    @PostMapping("is-eligible")
    public ResponseEntity<Boolean> isUserEligible(@RequestParam String userId,@RequestParam String listingType){

        return ResponseEntity.ok(userService.isEligible(userId,listingType));

    }
     //@PreAuthorize(value = )
     @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteById(@PathVariable Long id){
        return ResponseEntity.ok(new ApiResponse<>("success",userService.deleteById(id),"user removed"));
     }

     @PutMapping()
    public ResponseEntity<ApiResponse<BaseUserResponse>> update(@RequestBody UpdateUserRequest updateUserRequest){
        return ResponseEntity.ok(new ApiResponse<>("success",userService.update(updateUserRequest),"user updated"));
     }
     @PatchMapping()
    public ResponseEntity<ApiResponse<BaseUserResponse>> addBalance(@RequestParam BigDecimal amount){
        return ResponseEntity.ok(new ApiResponse<>("success",userService.addBalance(amount),"added"));
     }

     @GetMapping("test")
    public ResponseEntity<ResponseEntity<String>> test(@RequestParam String id){
        return ResponseEntity.ok(listingClient.getBuildingById(id));
     }
    @GetMapping("get-user-properties")
    public ResponseEntity<ApiResponse<ResponseEntity<Page<PropertyResponse>>>> getBuildings(@RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "size",defaultValue = "10") int size){
        Objects.requireNonNull(propertyClient.getUserProperties(CurrentUser.getPrincipal().getId(), page, size).getBody()).getContent().forEach(System.out::println);
        return ResponseEntity.ok(new ApiResponse<>("success", propertyClient.getUserProperties(CurrentUser.getPrincipal().getId(),page,size),"hmm"));
    }

    @GetMapping("get-users-packages")
    public ResponseEntity<ApiResponse<Page<UserPackagesResponse>>> getUsersPackages(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                                    @RequestParam(name = "size", defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new ApiResponse<>("success",userService.getUsersPackages(pageable),"all packages of user"));
    }
 //   @GetMapping("get-all-packages")
 //   public ResponseEntity<ApiResponse<List<Packages>>> getPackages(){
        // return ResponseEntity.ok(new ApiResponse<>("success",userService.getUserPackages(),"user's packages")); */
   // }


}
