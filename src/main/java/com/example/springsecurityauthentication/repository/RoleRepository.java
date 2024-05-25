package com.example.springsecurityauthentication.repository;

import com.example.springsecurityauthentication.enums.RoleName;
import com.example.springsecurityauthentication.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
    boolean existsByName(RoleName roleName);
}
