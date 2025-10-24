package com.akif.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BaseException {
    
    public static final String ERROR_CODE = "INVALID_TOKEN";
    
    public InvalidTokenException(String message) {
        super(ERROR_CODE, message, HttpStatus.UNAUTHORIZED);
    }

    public InvalidTokenException(String message, HttpStatus httpStatus) {
        super(ERROR_CODE, message, httpStatus);
    }
}
