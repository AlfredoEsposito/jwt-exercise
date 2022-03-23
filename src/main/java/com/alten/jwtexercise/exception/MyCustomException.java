package com.alten.jwtexercise.exception;

public class MyCustomException extends RuntimeException{

    public MyCustomException(String message) {
        super(message);
    }
}
