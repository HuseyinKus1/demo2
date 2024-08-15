package com.fmss.api_gateway.util;

import com.fmss.api_gateway.config.logger.MyLogger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static java.time.Duration.*;
@Component
public class LoggingFilter implements GlobalFilter {
    private static final MyLogger myLogger = (MyLogger) LoggerFactory.getLogger(LoggingFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Instant init = Instant.now();
        myLogger.rabbit("Path of the request received -> {}", exchange.getRequest().getPath());

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
         Instant finish = Instant.now();
            String duration = String.valueOf(between(init,finish).toMillis());
            myLogger.rabbit("UserId: {} , Execution time for request -> {} , CORRELATION-ID: {}, Status Code: {}",
                    exchange.getRequest().getHeaders().get("USER-ID"),
                    duration + " ms",
                    exchange.getRequest().getHeaders().get("CORRELATION-ID"),
                    exchange.getResponse().getStatusCode());
        }));
    }
}
