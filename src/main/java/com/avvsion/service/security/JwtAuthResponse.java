package com.avvsion.service.security;

import com.avvsion.service.model.Customers;
import com.avvsion.service.model.Sellers;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtAuthResponse {
    private String token;
    private Customers customer;
    private Sellers sellers;
}
