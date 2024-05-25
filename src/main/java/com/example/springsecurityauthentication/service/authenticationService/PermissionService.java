package com.example.springsecurityauthentication.service.authenticationService;

import com.example.springsecurityauthentication.enums.PermissionName;
import com.example.springsecurityauthentication.models.Permission;
import com.example.springsecurityauthentication.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public Optional<Permission> getPermissionByName(PermissionName name) {
        return permissionRepository.findByName(name);
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Permission createPermission(PermissionName name) {
        Permission permission = new Permission(name);
        return permissionRepository.save(permission);
    }

    public Optional<Permission> updatePermission(Long id, PermissionName name) {
        Optional<Permission> optionalPermission = permissionRepository.findById(id);
        if (optionalPermission.isPresent()) {
            Permission permission = optionalPermission.get();
            permission.setName(name);
            return Optional.of(permissionRepository.save(permission));
        } else {
            return Optional.empty();
        }
    }

    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }

    public Permission findByName(String name) {
        Optional<Permission> permissionOpt = permissionRepository.findByName(PermissionName.valueOf(name));
        return permissionOpt.orElseThrow(() -> new RuntimeException("Error: Permission is not found."));
    }
}
