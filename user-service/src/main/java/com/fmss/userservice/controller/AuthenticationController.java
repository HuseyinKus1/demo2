package com.fmss.userservice.controller;

import com.fmss.userservice.dto.ApiResponse;
import com.fmss.userservice.dto.request.AuthRequest;
import com.fmss.userservice.dto.response.AuthResponse;
import com.fmss.userservice.dto.request.RefreshTokenRequest;
import com.fmss.userservice.security.CustomUserDetails;
import com.fmss.userservice.security.JwtUtil;
import com.fmss.userservice.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthenticationController {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userService;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password()));
        CustomUserDetails user = userService.loadUserByUsername(authRequest.email());
        String jwt = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        return ResponseEntity.ok(AuthResponse.builder().accessToken(jwt).refreshToken(refreshToken).build());
    }


    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String email = jwtUtil.getEmailFromToken(refreshToken);
        if(jwtUtil.validateRefreshToken(refreshToken,email)){
            jwtUtil.revokeRefreshToken(refreshToken);
            refreshToken = jwtUtil.generateRefreshToken(email);
            String newAccessToken = jwtUtil.generateToken(email);
            return ResponseEntity.ok(new ApiResponse<>("success",AuthResponse.builder().accessToken(newAccessToken).refreshToken(refreshToken).build(),""));
        }
        return ResponseEntity.ok(new ApiResponse<>("failure",null,"invalid refresh token"));
    }
}