package com.example.springsecurityauthentication.models;

import com.example.springsecurityauthentication.enums.HttpMethod;
import com.example.springsecurityauthentication.enums.PermissionName;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "permission")
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @NaturalId
    private PermissionName name;


    public Permission() {
    }

    public Permission(PermissionName name) {
        this.name = name;
    }

    public PermissionName getName() {
        return name;
    }

    public void setName(PermissionName name) {
        this.name = name;
    }

    public HttpMethod getHttpMethod() {
        return name.getHttpMethod();
    }
}