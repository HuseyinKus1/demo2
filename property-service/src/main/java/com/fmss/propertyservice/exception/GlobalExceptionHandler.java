package com.fmss.propertyservice.exception;

import com.fmss.propertyservice.dto.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private static final String FAILED = "failed";
    private static final String ERR = "An error occurred: ";

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFoundException(PropertyNotFoundException ex, WebRequest request) {

        ApiResponse<String> response = new ApiResponse<>(FAILED, ex.getMessage(), request.getDescription(false));
        log.error(ERR, ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGlobalException(Exception ex, WebRequest request) {
        log.error(log.getClass().getName());
        log.error(ERR, ex);
        ApiResponse<String> response = new ApiResponse<>(FAILED, ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(PropertyNotEligibleException.class)
    public ResponseEntity<ApiResponse<String>> handleTargetAlreadyExistsException(PropertyNotEligibleException ex, WebRequest request) {
        log.error(ERR, ex);
        ApiResponse<String> response = new ApiResponse<>(FAILED, ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(PropertyNotEligibleException ex, WebRequest request) {
        log.error(ERR, ex);
        ApiResponse<String> response = new ApiResponse<>(FAILED, ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
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
