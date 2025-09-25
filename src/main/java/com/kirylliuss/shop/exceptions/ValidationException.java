package com.kirylliuss.shop.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends GlobalException {
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_GATEWAY);
    }
}
