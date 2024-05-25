package com.example.springsecurityauthentication.service.authenticationService;

import com.example.springsecurityauthentication.enums.PermissionName;
import com.example.springsecurityauthentication.enums.RoleName;
import com.example.springsecurityauthentication.exception.CustomRoleNotFoundException;
import com.example.springsecurityauthentication.models.Permission;
import com.example.springsecurityauthentication.models.Role;
import com.example.springsecurityauthentication.repository.PermissionRepository;
import com.example.springsecurityauthentication.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }


    @Transactional
    public Role createRole(RoleName roleName) {
        Role role = new Role(roleName);
        Set<Permission> permissions = new HashSet<>();

        if (roleName == RoleName.ROLE_ADMIN) {
            for (PermissionName permissionName : PermissionName.values()) {
                Optional<Permission> permissionOptional = permissionRepository.findByName(permissionName);
                Permission permission;
                if (permissionOptional.isPresent()) {
                    permission = permissionOptional.get();
                    System.out.println("Found existing permission: " + permissionName);
                } else {
                    permission = new Permission(permissionName);
                    permission = permissionRepository.save(permission);
                    System.out.println("Created new permission: " + permissionName);
                }
                permissions.add(permission);
            }

            role.setPermissions(permissions);
        }

        return roleRepository.save(role);
    }

    public Role findOrCreateRole(RoleName roleName) {
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        return optionalRole.orElseGet(() -> createRole(roleName));
    }


    public List<Role> getRolesForRole(RoleName roleName) {
        Role role = findOrCreateRole(roleName);
        return Collections.singletonList(role);
    }
}
