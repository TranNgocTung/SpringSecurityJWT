package com.example.springsecurityauthentication.exception.Notification;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse {
    private HttpStatus status;
    private String message;
    private String debugMessage;

    public ApiResponse(HttpStatus status, String message, String debugMessage) {
        this.status = status;
        this.message = message;
        this.debugMessage = debugMessage;
    }
}
