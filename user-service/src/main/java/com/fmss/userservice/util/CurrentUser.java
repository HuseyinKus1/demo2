package com.fmss.userservice.util;

import com.fmss.userservice.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

public class CurrentUser {
    private CurrentUser(){}
    public static CustomUserDetails getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("No authentication found in SecurityContext");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return (CustomUserDetails) principal;
        }else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
        }
    }

}
