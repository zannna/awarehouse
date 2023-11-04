package com.example.awarehouse.exception.exceptions;

public class UserUnauthorized extends RuntimeException{
    public UserUnauthorized(String message) {
        super(message);
    }
}
