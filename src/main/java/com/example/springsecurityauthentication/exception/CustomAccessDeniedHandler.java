package com.example.springsecurityauthentication.exception;

import com.example.springsecurityauthentication.exception.Notification.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        logger.error("Access Denied Handler: Forbidden error: {}", accessDeniedException.getMessage());

        ApiResponse apiResponse = new ApiResponse(HttpStatus.FORBIDDEN, "Access Denied", accessDeniedException.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
