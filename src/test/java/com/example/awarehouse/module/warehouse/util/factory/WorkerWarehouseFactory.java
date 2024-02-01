package com.example.awarehouse.module.warehouse.util.factory;

import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.module.warehouse.WorkerWarehouse;

import static com.example.awarehouse.module.warehouse.util.factory.WarehouseFactory.createSecondWarehouse;
import static com.example.awarehouse.module.warehouse.util.factory.WarehouseFactory.createWarehouse;
import static com.example.awarehouse.module.auth.util.factory.WorkerFactory.createSecondWorker;
import static com.example.awarehouse.module.auth.util.factory.WorkerFactory.createWorker;

public class WorkerWarehouseFactory {

        public static WorkerWarehouse createWorkerWarehouse() {
            return new WorkerWarehouse(1L, createWorker(), createWarehouse(), Role.WORKER);
        }

    public static WorkerWarehouse createSecondWorkerWarehouse() {
        return new WorkerWarehouse(2L, createSecondWorker(), createSecondWarehouse(), Role.WORKER);
    }
}
