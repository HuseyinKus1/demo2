package com.fmss.userservice.dto.request;


import lombok.Builder;

@Builder
public record AuthRequest(String email, String password) {}

