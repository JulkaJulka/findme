package com.findme.exception;

public class LimitExceed extends BadRequestException {
    public LimitExceed(String message) {
        super(message);}
}
