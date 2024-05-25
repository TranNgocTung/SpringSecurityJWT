package com.example.springsecurityauthentication.service.authenticationService;

import com.example.springsecurityauthentication.enums.PermissionName;
import com.example.springsecurityauthentication.enums.RoleName;
import com.example.springsecurityauthentication.exception.ErrorResponse;
import com.example.springsecurityauthentication.models.Permission;
import com.example.springsecurityauthentication.models.Role;
import com.example.springsecurityauthentication.models.User;
import com.example.springsecurityauthentication.payload.request.LoginRequest;
import com.example.springsecurityauthentication.payload.request.SignupRequest;
import com.example.springsecurityauthentication.payload.response.JwtResponse;
import com.example.springsecurityauthentication.payload.response.MessageResponse;
import com.example.springsecurityauthentication.repository.RoleRepository;
import com.example.springsecurityauthentication.repository.UserRepository;
import com.example.springsecurityauthentication.security.jwt.JwtProvider;
import com.example.springsecurityauthentication.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    private RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    @Transactional
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            logger.debug("User {} authenticated successfully with roles: {}", userDetails.getUsername(), userDetails.getAuthorities());

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getRoles()));
        } catch (UsernameNotFoundException e) {
            logger.error("User not found with username: {}", loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid credentials"));
        } catch (Exception e) {
            logger.error("Internal server error during authentication", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleService.findOrCreateRole(RoleName.ROLE_USER);
            roles.add(userRole);
        } else {
            for (String role : strRoles) {
                switch (role) {
                    case "admin":
                        Optional<User> existingAdminOptional = userRepository.findByRoles_Name(RoleName.ROLE_ADMIN);
                        if (existingAdminOptional.isPresent()) {
                            User existingAdmin  = existingAdminOptional.get();
                            Role adminRole = roleService.findOrCreateRole(RoleName.ROLE_ADMIN);
                            existingAdmin.getRoles().add(adminRole);
                            userRepository.save(existingAdmin);
                            return ResponseEntity
                                    .status(HttpStatus.OK)
                                    .body(new MessageResponse("Admin already exists"));
                        } else {
                            Role adminRole = roleService.findOrCreateRole(RoleName.ROLE_ADMIN);
                            User newAdmin = new User(signUpRequest.getUsername(),
                                    signUpRequest.getEmail(),
                                    encoder.encode(signUpRequest.getPassword()));
                            newAdmin.getRoles().add(adminRole);
                            userRepository.save(newAdmin);
                            return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
                        }

                    case "moderator":
                        Role moderatorRole = roleService.findOrCreateRole(RoleName.ROLE_MODERATOR);
                        roles.add(moderatorRole);
                        break;
                    default:
                        Role userRole = roleService.findOrCreateRole(RoleName.ROLE_USER);
                        roles.add(userRole);
                }
            }
        }

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}