package com.findme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//TODO all exception should be moved to separate repository
public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
