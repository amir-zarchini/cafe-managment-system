package com.example.cafemanagmentsystem.exception;

public class SomethingWentWrongException extends RuntimeException{

    private String message;

    public SomethingWentWrongException() {
    }

    public SomethingWentWrongException(String message) {
        super(message);
        this.message = message;
    }
}
