package com.example.springsecurityauthentication.repository;

import com.example.springsecurityauthentication.enums.RoleName;
import com.example.springsecurityauthentication.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    boolean existsByRolesName(RoleName roleName);

    @Query("SELECT u FROM User u JOIN FETCH u.roles")
    List<User> findAllUsersWithRoles();

   Optional<User> findByRoles_Name(RoleName roleName);

}
