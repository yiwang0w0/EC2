package com.example.ecbackend.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Response<String> handleException(Exception e) {
        e.printStackTrace();
        return Response.error(e.getMessage());
    }
}
