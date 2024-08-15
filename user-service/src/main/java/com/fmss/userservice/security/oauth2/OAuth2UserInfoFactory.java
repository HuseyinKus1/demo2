package com.fmss.userservice.security.oauth2;

import com.fmss.userservice.model.enums.AuthProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;
import java.util.Set;

public class OAuth2UserInfoFactory {

    private OAuth2UserInfoFactory(){}

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {

        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        else {
            throw new OAuth2AuthenticationException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}