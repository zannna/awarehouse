package com.example.awarehouse.module.auth.util.factory;

import com.example.awarehouse.module.auth.Worker;

import static com.example.awarehouse.module.auth.util.WorkerConstants.WORKER_ID;
import static com.example.awarehouse.module.auth.util.WorkerConstants.WORKER_ID_2;

public class WorkerFactory {

        public static Worker createWorker() {
            return new Worker(WORKER_ID , "John", "Doe");
        }

    public static Worker createSecondWorker() {
        return new Worker(WORKER_ID_2 , "Jenny", "Smith");
    }
}
