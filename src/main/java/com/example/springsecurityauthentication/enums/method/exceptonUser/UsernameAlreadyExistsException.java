package com.example.springsecurityauthentication.enums.method.exceptonUser;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}