package com.example.springsecurityauthentication.controllers;

import com.example.springsecurityauthentication.exception.ErrorResponse;
import com.example.springsecurityauthentication.payload.request.LoginRequest;
import com.example.springsecurityauthentication.payload.request.SignupRequest;
import com.example.springsecurityauthentication.service.authenticationService.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            return authService.authenticateUser(loginRequest);
        } catch (Exception e) {
            logger.error("User authentication failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            return authService.registerUser(signUpRequest);
        } catch (Exception e) {
            logger.error("User authentication failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage()));
        }
    }
}
