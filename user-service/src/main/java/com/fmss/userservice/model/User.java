package com.fmss.userservice.model;


import com.fmss.userservice.model.enums.AuthProvider;
import com.fmss.userservice.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends AbstractBaseModel implements Serializable {
    @Column(unique = true)
    private String username;
    private String password;

    public Collection<? extends GrantedAuthority> setAuthorities(Role role) {
        return List.of(role);
    }
    @Email
    @Column(unique = true)
    private String email;
    private String name;
    private String lastName;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
    private String authProviderId;
    @OneToMany
    private List<UserPackages> userPackages;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private boolean isAccountNonExpired = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean isAccountNonLocked = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean isCredentialsNonExpired = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean isEnabled = true;

    @Column( precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;


}
