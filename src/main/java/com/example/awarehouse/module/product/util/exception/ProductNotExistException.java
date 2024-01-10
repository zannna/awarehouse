package com.example.awarehouse.module.product.util.exception;

public class ProductNotExistException extends RuntimeException{
    public ProductNotExistException(String message) {
        super(message);
    }
}
