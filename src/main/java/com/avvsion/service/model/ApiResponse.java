package com.avvsion.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {
    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    private String message;
    private boolean success;
}