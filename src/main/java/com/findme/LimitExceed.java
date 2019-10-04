package com.findme;

public class LimitExceed extends BadRequestException {
    public LimitExceed(String message) {
        super(message);}
}
