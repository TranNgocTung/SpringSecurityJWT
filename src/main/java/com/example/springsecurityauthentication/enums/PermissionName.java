package com.example.springsecurityauthentication.enums;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public enum PermissionName {
    @Enumerated(EnumType.STRING)
    CREATE,
    @Enumerated(EnumType.STRING)
    UPDATE,
    @Enumerated(EnumType.STRING)
    DELETE,
    @Enumerated(EnumType.STRING)
    READ;
    public HttpMethod getHttpMethod() {
        switch (this) {
            case CREATE:
                return HttpMethod.POST;
            case UPDATE:
                return HttpMethod.PUT;
            case DELETE:
                return HttpMethod.DELETE;
            case READ:
                return HttpMethod.GET;
            default:
                throw new IllegalArgumentException("Unknown permission");
        }
    }
}

