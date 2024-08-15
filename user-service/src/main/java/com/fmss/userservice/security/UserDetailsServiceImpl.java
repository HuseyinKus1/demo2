package com.fmss.userservice.security;



import com.fmss.userservice.constants.ExceptionConstants;
import com.fmss.userservice.model.User;
import com.fmss.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionConstants.USER_NOT_FOUND));
        return mapUserToCustomUserDetails(user);
    }

    private CustomUserDetails mapUserToCustomUserDetails(User user) {
        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.isEnabled(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired()
        );
    }

    public List<String> jwtEnricher(String email) {
        List<String> jwtFiller = new ArrayList<>();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        jwtFiller.add(user.getId().toString());
        jwtFiller.add(user.getRole().toString());
        return jwtFiller;
    }
}