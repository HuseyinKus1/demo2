package com.fmss.userservice.security;

import com.fmss.userservice.model.User;
import com.fmss.userservice.model.enums.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails, OAuth2User {
    @Getter
    private Long id;
    private String name;
    private String username;
    @Getter
    private String email;
    private String password;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private transient Map<String,Object> attributes;
    @Getter
    private Role role;

    public CustomUserDetails(Long id, String username,String email, String password,Role role, boolean enabled, boolean accountNonLocked, boolean accountNonExpired, boolean credentialsNonExpired) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }
    public CustomUserDetails(User user){
        CustomUserDetails.CustomUserDetailsBuilder oauth2Principal = CustomUserDetails.builder();
        oauth2Principal.id(user.getId());
        oauth2Principal.email(user.getEmail());
        oauth2Principal.password(user.getPassword());
        oauth2Principal.role(user.getRole());
        oauth2Principal.credentialsNonExpired(user.isCredentialsNonExpired());
        oauth2Principal.accountNonLocked(user.isAccountNonLocked());
        oauth2Principal.accountNonExpired(user.isAccountNonExpired());
        oauth2Principal.enabled(user.isEnabled());
        oauth2Principal.build();
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return email;
    }
}