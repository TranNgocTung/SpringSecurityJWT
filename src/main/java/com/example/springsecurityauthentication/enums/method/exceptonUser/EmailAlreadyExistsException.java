package com.example.springsecurityauthentication.enums.method.exceptonUser;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}