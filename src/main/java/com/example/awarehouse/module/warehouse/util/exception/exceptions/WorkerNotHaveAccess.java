package com.example.awarehouse.module.warehouse.util.exception.exceptions;

public class WorkerNotHaveAccess extends RuntimeException{
    public WorkerNotHaveAccess(String message) {
        super(message);
    }
}
