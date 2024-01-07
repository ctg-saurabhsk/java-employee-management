package com.humanresource.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException1 extends RuntimeException {

    public ResourceNotFoundException1(String message) {
        super(message);
    }

    public ResourceNotFoundException1(String message, Throwable cause) {
        super(message, cause);
    }
}