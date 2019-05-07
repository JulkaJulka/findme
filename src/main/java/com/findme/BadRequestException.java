package com.findme;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
