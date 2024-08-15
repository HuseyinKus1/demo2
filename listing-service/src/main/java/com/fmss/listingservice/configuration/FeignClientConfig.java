package com.fmss.listingservice.configuration;

import feign.Logger;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}

class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 403 -> new ForbiddenException("Access denied to user service");
            default -> new Exception("Generic error");
        };
    }
}