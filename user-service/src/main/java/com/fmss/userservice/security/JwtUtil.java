package com.fmss.userservice.security;



import com.fmss.userservice.constants.ExceptionConstants;
import com.fmss.userservice.exception.ResourceNotFoundException;
import com.fmss.userservice.mapper.RefreshTokenMapper;
import com.fmss.userservice.model.RefreshToken;
import com.fmss.userservice.model.enums.RefreshTokenStatus;
import com.fmss.userservice.model.User;
import com.fmss.userservice.repository.RefreshTokenRepository;
import com.fmss.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {
    @Value("${jwt.key}")
    private String secretKey;
    @Value("${access.token.expiration}")
    private long jwtExpirationInMs;
    @Value("${refresh.token.expiration}")
    private long refreshExpirationInMs;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final RefreshTokenMapper refreshTokenMapper;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public JwtUtil(RefreshTokenRepository refreshTokenRepository, RefreshTokenMapper refreshTokenMapper,
                   UserRepository userRepository, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenMapper = refreshTokenMapper;
        this.userRepository = userRepository;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    public String generateToken(String email) {
        List<String> requiredClaims = userDetailsServiceImpl.jwtEnricher(email);
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", requiredClaims.getFirst())
                .claim("role", requiredClaims.getLast())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpirationInMs);
        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(ExceptionConstants.USER_NOT_FOUND));
        refreshTokenRepository.save(refreshTokenMapper.toRefreshToken
                (refreshToken, user, expiryDate.toInstant(), RefreshTokenStatus.ACTIVE));
        return refreshToken;
    }

    public boolean validateAccessToken(String token, String email) {
        return email.equals(getEmailFromToken(token)) && !isTokenExpired(token);}

    public boolean validateRefreshToken(String token, String email) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(()
                -> new ResourceNotFoundException(ExceptionConstants.REFRESH_NOT_EXIST));
        Instant instant = Instant.now();
        if(refreshToken.getExpirationDate().isBefore(instant)){
            refreshToken.setRefreshTokenStatus(RefreshTokenStatus.EXPIRED);
            refreshTokenRepository.save(refreshToken);
        }
        return Objects.equals(refreshToken.getUser().getEmail(), email)
                && refreshToken.getRefreshTokenStatus().equals(RefreshTokenStatus.ACTIVE);
    }

    public void revokeRefreshToken(String token) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByToken(token);

        optionalRefreshToken.ifPresent(refreshToken -> refreshToken.setRefreshTokenStatus(RefreshTokenStatus.INACTIVATED));
    }

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }
}