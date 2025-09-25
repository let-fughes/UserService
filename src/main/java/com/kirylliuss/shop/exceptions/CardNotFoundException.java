package com.kirylliuss.shop.exceptions;

import org.springframework.http.HttpStatus;

public class CardNotFoundException extends GlobalException {
    public CardNotFoundException(Long id) {
        super("Card with id not found: " + id, HttpStatus.NOT_FOUND);
    }

    public CardNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
