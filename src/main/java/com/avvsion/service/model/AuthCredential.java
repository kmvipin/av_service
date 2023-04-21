package com.avvsion.service.model;

import lombok.Data;

@Data
public class AuthCredential {
    private String email;
    private String pass;
    private String role;
}
