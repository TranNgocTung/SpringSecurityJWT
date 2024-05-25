package com.example.springsecurityauthentication.payload.response;

import lombok.Data;

import java.util.List;
@Data
public class RoleResponse {
    private String role;
    private List<String> authorities;

    public RoleResponse(String role, List<String> authorities) {
        this.role = role;
        this.authorities = authorities;
    }

}