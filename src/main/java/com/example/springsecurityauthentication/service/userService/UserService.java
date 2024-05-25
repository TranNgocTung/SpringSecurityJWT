package com.example.springsecurityauthentication.service.userService;


import com.example.springsecurityauthentication.enums.RoleName;
import com.example.springsecurityauthentication.enums.method.exceptonUser.EmailAlreadyExistsException;
import com.example.springsecurityauthentication.enums.method.exceptonUser.UsernameAlreadyExistsException;
import com.example.springsecurityauthentication.models.Permission;
import com.example.springsecurityauthentication.models.Role;
import com.example.springsecurityauthentication.models.User;
import com.example.springsecurityauthentication.repository.RoleRepository;
import com.example.springsecurityauthentication.repository.UserRepository;
import com.example.springsecurityauthentication.service.authenticationService.PermissionService;
import com.example.springsecurityauthentication.service.authenticationService.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PermissionService permissionService;

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAllUsersWithRoles();
    }

    public Optional<User> createUser(String username, String email, String password, Set<String> strRoles) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Error: Email is already in use!");
        }
        boolean isAdminExists = userRepository.existsByRolesName(RoleName.ROLE_ADMIN);
        if (isAdminExists) {
            throw new RuntimeException("Error: An admin already exists. Cannot create another admin.");
        }
        User user = new User(username, email, passwordEncoder.encode(password));

        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(RoleName.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return Optional.of(user);
    }

    public Optional<User> updateUser(Long id, String username, String email, Set<String> strRoles) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(username);
            user.setEmail(email);

            Set<Role> roles = new HashSet<>();
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(RoleName.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });

            user.setRoles(roles);
            userRepository.save(user);

            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }


    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByUsername(name);
    }


    public void save(User user) {
        userRepository.save(user);
    }


    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return userRepository.findByUsername(userDetails.getUsername());
        }
        return Optional.empty();
    }

    public void assignAdminPermissions(User admin) {
        Collection<? extends Role> adminRoles = roleService.getRolesForRole(RoleName.ROLE_ADMIN);
        admin.getRoles().addAll(adminRoles);
    }

}