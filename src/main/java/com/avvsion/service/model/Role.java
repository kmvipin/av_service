package com.avvsion.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Role {
    @JsonIgnore
    private int roleId;

    private String roleName;

    public Role(){

    }
    public Role(String roleName) {
        this.roleName = roleName;
    }
}
