package com.akif.exception;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends BaseException {
    
    public static final String ERROR_CODE = "TOKEN_EXPIRED";
    
    public TokenExpiredException(String message) {
        super(ERROR_CODE, message, HttpStatus.UNAUTHORIZED);
    }

    public TokenExpiredException(String message, HttpStatus httpStatus) {
        super(ERROR_CODE, message, httpStatus);
    }
}
