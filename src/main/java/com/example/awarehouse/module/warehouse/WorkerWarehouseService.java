package com.example.awarehouse.module.warehouse;

import com.example.awarehouse.module.warehouse.dto.WarehouseListResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WorkerWarehouseService {
    WorkerWarehouseRepository workerWarehouseRepository;

    public WorkerWarehouse newRelation(UUID warehouseId, UUID workerId, Role role) {
        WorkerWarehouse workerWarehouse = workerWarehouseRepository.createWorkerWarehouseAssociation(warehouseId, workerId, role.name());
        return workerWarehouse;
    }

    public List<WarehouseListResponseDto> getWarehouses(UUID workerId) {
        return workerWarehouseRepository.findWorkerWarehouses(workerId).orElseGet(()-> new ArrayList<>());
    }
}
