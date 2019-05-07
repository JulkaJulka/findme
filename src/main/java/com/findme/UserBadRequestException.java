package com.findme;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User already exist in DB")
public class UserBadRequestException extends Exception {
    public UserBadRequestException(String message) {
        super(message);
    }
}
