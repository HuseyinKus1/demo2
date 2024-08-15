package com.fmss.userservice.security.oauth2;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;
import java.util.Set;

@Getter
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;
    protected OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();
    public abstract String getName();
    public abstract String getLastName();
    public abstract String getEmail();
    public abstract String getImageUrl();
}