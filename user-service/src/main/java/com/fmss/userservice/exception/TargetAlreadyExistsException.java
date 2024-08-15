package com.fmss.userservice.exception;

public class TargetAlreadyExistsException extends RuntimeException{
    public TargetAlreadyExistsException(String message) {
        super(message);
    }
}
