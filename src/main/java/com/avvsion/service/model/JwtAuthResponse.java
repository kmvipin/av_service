package com.avvsion.service.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtAuthResponse {
    private String token;
    private boolean success;
    private String message;
}
