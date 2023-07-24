package com.example.cafemanagmentsystem.exception;

public class UnauthorizedAccessException extends RuntimeException{

    private String message;

    public UnauthorizedAccessException() {
    }

    public UnauthorizedAccessException(String message) {
        super(message);
        this.message = message;
    }
}
