package com.example.springsecurityauthentication.enums.method.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UpdateUserRequest {
    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> roles;

    public UpdateUserRequest() {
    }

    public UpdateUserRequest(String username, String email, Set<String> roles) {
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

}