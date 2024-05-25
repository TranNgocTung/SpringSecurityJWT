package com.example.springsecurityauthentication.repository;

import com.example.springsecurityauthentication.enums.PermissionName;
import com.example.springsecurityauthentication.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(PermissionName name);
    List<Permission> findAllByNameIn(List<PermissionName> names);
    boolean existsByName(PermissionName name);

}