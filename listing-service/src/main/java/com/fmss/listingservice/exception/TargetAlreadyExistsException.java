package com.fmss.listingservice.exception;

public class TargetAlreadyExistsException extends RuntimeException{
    public TargetAlreadyExistsException(String message) {
        super(message);
    }
}
