package com.example.cafemanagmentsystem.exception;

public class InvalidDataException extends RuntimeException{

    private String message;
    public InvalidDataException() {
    }

    public InvalidDataException(String message) {
        super(message);
        this.message = message;
    }
}
