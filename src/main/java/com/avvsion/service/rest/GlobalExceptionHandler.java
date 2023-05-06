package com.avvsion.service.rest;

import com.avvsion.service.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException ex) {
        String message = ex.getMessage();
        ex.printStackTrace();
        ApiResponse apiResponse = new ApiResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiResponse> exceptionHandler(Exception exception){
        exception.printStackTrace();
        ApiResponse response = new ApiResponse(exception.getMessage(),false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}