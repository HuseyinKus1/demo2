package com.fmss.api_gateway.security;

import jakarta.annotation.Nonnull;
import org.springframework.boot.web.reactive.filter.OrderedWebFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtAuthenticationWebFilter implements OrderedWebFilter {
    private static final int PRECEDENCE = Ordered.HIGHEST_PRECEDENCE;

    private final JwtUtil jwtUtil;

    public JwtAuthenticationWebFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Nonnull
    public Mono<Void> filter(ServerWebExchange exchange,@Nonnull WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.getEmailFromToken(jwt);
            String role = jwtUtil.extractRole(jwt);
            if (username != null && jwtUtil.validateAccessToken(jwt, username)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null,  Collections.singletonList(Role.valueOf(role)));
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            }
        }
        return chain.filter(exchange);
    }
    @Override
    public int getOrder() {
        return PRECEDENCE;
    }
}