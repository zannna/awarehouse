package com.example.awarehouse.module.warehouse;

import com.example.awarehouse.module.administration.dto.AdminWorkersDto;
import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import com.example.awarehouse.module.warehouse.dto.GroupWarehouseDto;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WorkerWarehouseRelationNotExist;
import com.example.awarehouse.util.UserIdSupplier;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WorkerWarehouseService {
    WorkerWarehouseRepository workerWarehouseRepository;
    UserIdSupplier workerIdSupplier;

    public WorkerWarehouse newRelation(UUID warehouseId, UUID workerId, Role role) {
        WorkerWarehouse workerWarehouse = workerWarehouseRepository.createWorkerWarehouseAssociation(warehouseId, workerId, role.name());
        return workerWarehouse;
    }

    public List<BasicWarehouseInfoDto> getWarehouses(UUID workerId) {
        return workerWarehouseRepository.findWorkerWarehousesBasicInformation(workerId).stream()
                .map((w)->new BasicWarehouseInfoDto(w.getId(), w.getName(), null)).collect(Collectors.toList());
    }
    public Set<WorkerWarehouse> getWorkerWarehouses(UUID workerId){
        return workerWarehouseRepository.findWorkerWarehouses(workerId);
    }

    public void validateWorkerWarehouseRelation(List<UUID> warehouseIds){
        UUID workerId = workerIdSupplier.getUserId();
        for (UUID warehouseId: warehouseIds) {
            validateWorkerWarehouseRelation(workerId, warehouseId);
        }
    }
    public void validateWorkerWarehouseRelation(UUID warehouseId){
        validateWorkerWarehouseRelation(workerIdSupplier.getUserId(), warehouseId);
    }

    public void validateWorkerWarehouseRelation(UUID workerId, UUID warehouseId){
        boolean isWorkerWarehouseRelationExist = workerWarehouseRepository.findByWarehouseIdAndWorkerId(warehouseId, workerId).isPresent();
        if(!isWorkerWarehouseRelationExist){
            throw new WorkerWarehouseRelationNotExist("Worker with id "+workerId+" does not have relation with warehouse with id "+warehouseId);
        }

    }

    public List<GroupWarehouseDto> getWarehouseGroups(UUID warehouseId, UUID workerId){
        return   workerWarehouseRepository.findWorkerWarehousesWithGroups(warehouseId, workerId);
    }

    public List<AdminWorkersDto> getAdminWarehouses(UUID workerId) {
        Set<WorkerWarehouse> workerWarehouses = workerWarehouseRepository.findWorkers(workerId, Role.ADMIN);

        List<AdminWorkersDto> adminWorkersDtos = workerWarehouses.stream()
                .collect(Collectors.groupingBy(WorkerWarehouse::getWarehouse, Collectors.toList()))
                .entrySet()
                .stream()
                .map(entry -> new AdminWorkersDto(
                        entry.getKey().getName(),
                        entry.getKey().getId(),
                        entry.getValue().stream()
                                .map(warehouseWorker -> new AdminWorkersDto.Worker(
                                        warehouseWorker.getWorker().getId(),
                                        warehouseWorker.getId(),
                                        warehouseWorker.getWorker().getFirstName(),
                                        warehouseWorker.getWorker().getLastName(),
                                        warehouseWorker.getRole().toString().toLowerCase()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return adminWorkersDtos;
    }

    public Optional<WorkerWarehouse> findWorkerWarehouse(UUID id) {
        return workerWarehouseRepository.findById(id);
    }

    public List<WorkerWarehouse> findWorkerWarehouse(List<UUID> workerEntityIds) {
        return workerWarehouseRepository.findAllById(workerEntityIds);
    }

    public void deleteWorkerWarehouses(List<WorkerWarehouse> workerWarehouses) {
        workerWarehouseRepository.deleteAllByIdInBatch(workerWarehouses.stream().map(WorkerWarehouse::getId).collect(Collectors.toList()));
    }
}
