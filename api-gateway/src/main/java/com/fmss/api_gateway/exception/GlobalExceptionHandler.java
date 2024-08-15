package com.fmss.api_gateway.exception;

import com.fmss.api_gateway.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private static final String FAILED = "failed";
    private static final String ERR = "An error occurred: ";


    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponse<String>>> handleGlobalException(Exception ex) {
        ApiResponse<String> apiResponse = new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                ex.getMessage(),
                "An unexpected error occurred"
        );
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error(ERR, ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse<String> response = new ApiResponse<>(FAILED, ex.getMessage(),errors.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
