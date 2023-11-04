package com.example.awarehouse.module.worker.util.factory;

import com.example.awarehouse.module.worker.Worker;

import static com.example.awarehouse.module.worker.util.WorkerConstants.WORKER_ID;

public class WorkerFactory {

        public static Worker createWorker() {
            return new Worker(WORKER_ID , "John", "Doe");
        }
}
