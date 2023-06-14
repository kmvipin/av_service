package com.avvsion.service.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ApiResponse {
    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    private String message;
    private boolean success;
}