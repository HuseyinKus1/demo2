package com.fmss.api_gateway.security;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.filter.OrderedWebFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthenticatedRequestEnricher implements OrderedWebFilter {
    private static final int PRECEDENCE = Ordered.HIGHEST_PRECEDENCE + 1;
    private final JwtUtil jwtUtil;
    @Nonnull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @Nonnull WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String jwt;
        String userId;
        String role;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            String correlationId = UUID.randomUUID().toString();
            userId = jwtUtil.extractId(jwt);
            role = jwtUtil.extractRole(jwt);
                   ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .header("USER-ID", userId)
                           .header("CORRELATION-ID",correlationId)
                           .header("ROLE",role)
                        .build();
                return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }
        return chain.filter(exchange);
    }
    @Override
    public int getOrder() {
        return PRECEDENCE;
    }
}
