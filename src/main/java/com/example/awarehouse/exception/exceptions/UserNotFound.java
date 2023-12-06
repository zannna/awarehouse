package com.example.awarehouse.exception.exceptions;

public class UserNotFound  extends RuntimeException{
    public UserNotFound(String message) {
        super(message);
    }
}
