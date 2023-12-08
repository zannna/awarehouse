package com.example.awarehouse.module.warehouse;

import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WorkerWarehouseRelationNotExist;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WorkerWarehouseService {
    WorkerWarehouseRepository workerWarehouseRepository;

    public WorkerWarehouse newRelation(UUID warehouseId, UUID workerId, Role role) {
        WorkerWarehouse workerWarehouse = workerWarehouseRepository.createWorkerWarehouseAssociation(warehouseId, workerId, role.name());
        return workerWarehouse;
    }

    public List<BasicWarehouseInfoDto> getWarehouses(UUID workerId) {
        return workerWarehouseRepository.findWorkerWarehousesBasicInformation(workerId).orElseGet(()-> new ArrayList<>());
    }
    public Set<Warehouse> getWorkerWarehouses(UUID workerId){
        return workerWarehouseRepository.findWorkerWarehouses(workerId);
    }
    public void validateWorkerWarehouseRelation(UUID workerId, UUID warehouseId){
        boolean isWorkerWarehouseRelationExist = workerWarehouseRepository.findByWarehouseIdAndWorkerId(warehouseId, workerId).isPresent();
        if(!isWorkerWarehouseRelationExist){
            throw new WorkerWarehouseRelationNotExist("Worker with id "+workerId+" does not have relation with warehouse with id "+warehouseId);
        }

    }
}
