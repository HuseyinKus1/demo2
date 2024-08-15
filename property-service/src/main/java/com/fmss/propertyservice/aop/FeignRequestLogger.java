package com.fmss.propertyservice.aop;

import jakarta.servlet.ServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.UUID;

@Aspect
@Order(0)
public class FeignRequestLogger {
    private static final String REQUEST_ID = "requestId";
    public static final String USER_ID = "userId";
    private static final String ANONYMOUS = "anonymous";
    @Around("(@within(org.springframework.web.bind.annotation.RestController) || " +
            "@within(org.springframework.stereotype.Controller)) && " +
            "!within(org.springdoc..*)")
    public Object loggingAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Instant init = Instant.now();
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String userId = attributes.getRequest().getHeader("USER-ID");
            return joinPoint.proceed();
        } finally {
            MDC.clear();
        }
}}
