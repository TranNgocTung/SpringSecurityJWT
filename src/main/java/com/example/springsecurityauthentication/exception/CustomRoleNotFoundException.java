package com.example.springsecurityauthentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomRoleNotFoundException extends RuntimeException {

    public CustomRoleNotFoundException(String message) {
        super(message);
    }
}