package com.example.awarehouse.module.warehouse;

import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface WorkerWarehouseRepository extends JpaRepository<WorkerWarehouse, UUID> {

    @Query(value = "insert into worker_warehouse (warehouse_id, worker_id, role) values (:warehouseId, :workerId, :role) RETURNING *", nativeQuery = true)
    WorkerWarehouse createWorkerWarehouseAssociation(UUID warehouseId, UUID workerId, String role);

    Optional<WorkerWarehouse> findByWarehouseIdAndWorkerId(UUID warehouseId, UUID workerId);

    @Query("select ww.warehouse.id, ww.warehouse.name from WorkerWarehouse ww where ww.worker.id = :workerId")
    Optional<List<BasicWarehouseInfoDto>> findWorkerWarehousesBasicInformation(UUID workerId);

    @Query("select distinct ww.warehouse from WorkerWarehouse ww where ww.worker.id =:workerId ")
    Set<Warehouse> findWorkerWarehouses(UUID workerId);

    @Query("select ww.warehouse from WorkerWarehouse ww " +
            "join ww.warehouse.warehouseGroups g" +
            "where g in (select warehouse.warehouseGroups ")
    Set<Warehouse> findWarehouseGroups(UUID workerId);
}
