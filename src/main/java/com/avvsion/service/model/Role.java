package com.avvsion.service.model;

import javax.persistence.*;

import lombok.Data;

@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    private int roleId;

    private String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
