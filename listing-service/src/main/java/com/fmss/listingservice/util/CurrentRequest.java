package com.fmss.listingservice.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class CurrentRequest {
    private static final String ROLE = "ROLE_ADMIN";
    private CurrentRequest(){}
    public static Long getUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Long.valueOf(Objects.requireNonNull(attributes).getRequest().getHeader("USER-ID"));
    }
    public static String getCorrelationId(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(attributes).getRequest().getHeader("CORRELATION-ID");
    }
    public static boolean isAdmin(){
        ServletRequestAttributes attributes = Objects.requireNonNull((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        String role =attributes.getRequest().getHeader("ROLE");
        return role.equals(ROLE);
    }
}
