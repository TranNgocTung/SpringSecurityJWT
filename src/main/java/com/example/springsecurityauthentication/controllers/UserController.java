package com.example.springsecurityauthentication.controllers;


import com.example.springsecurityauthentication.enums.method.user.CreateUserRequest;
import com.example.springsecurityauthentication.enums.method.user.UpdateUserRequest;
import com.example.springsecurityauthentication.models.User;
import com.example.springsecurityauthentication.service.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
    @RequestMapping("/api/permissions")
public class UserController {

    @Autowired
    private UserService userService;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userService.getUserById(id);
        return optionalUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        Optional<User> optionalUser = userService.createUser(createUserRequest.getUsername(),
                createUserRequest.getEmail(), createUserRequest.getPassword(), createUserRequest.getRoles());
        return optionalUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasPermission('UPDATE','ADMIN','MODERATOR','USER')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        Optional<User> optionalUser = userService.updateUser(id, updateUserRequest.getUsername(),
                updateUserRequest.getEmail(), updateUserRequest.getRoles());
        return optionalUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasPermission('DELETE','ADMIN','MODERATOR','USER')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}