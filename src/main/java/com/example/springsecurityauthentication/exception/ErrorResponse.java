package com.example.springsecurityauthentication.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private String error;

    public ErrorResponse(int status, String message, String error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }
}
