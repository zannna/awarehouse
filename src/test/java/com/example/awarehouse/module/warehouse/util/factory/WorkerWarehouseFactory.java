package com.example.awarehouse.module.warehouse.util.factory;

import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.module.warehouse.WorkerWarehouse;

import java.util.UUID;

import static com.example.awarehouse.module.warehouse.util.factory.WarehouseFactory.createSecondWarehouse;
import static com.example.awarehouse.module.warehouse.util.factory.WarehouseFactory.createWarehouse;
import static com.example.awarehouse.module.auth.util.factory.WorkerFactory.createSecondWorker;
import static com.example.awarehouse.module.auth.util.factory.WorkerFactory.createWorker;

public class WorkerWarehouseFactory {

        public static WorkerWarehouse createWorkerWarehouse() {
            return new WorkerWarehouse(UUID.fromString("2fb03da2-6ba9-4239-a5bf-6dd6e91a6802"), createWorker(), createWarehouse(), Role.WORKER);
        }

    public static WorkerWarehouse createSecondWorkerWarehouse() {
        return new WorkerWarehouse(UUID.fromString("2fb03da2-6ba9-4239-a5bf-6dd6e91a6802"), createSecondWorker(), createSecondWarehouse(), Role.WORKER);
    }
}
