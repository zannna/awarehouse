package com.example.awarehouse.module.warehouse;

import com.example.awarehouse.module.warehouse.dto.GroupWarehouseDto;
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

    @Query("select ww.warehouse from WorkerWarehouse ww where ww.worker.id = :workerId")
    List<Warehouse> findWarehouses(UUID workerId);

    @Query("select distinct ww from WorkerWarehouse ww where ww.worker.id =:workerId ")
    Set<WorkerWarehouse> findWorkerWarehouses(UUID workerId);

    @Query("select distinct ww from WorkerWarehouse ww where ww.warehouse.id in " +
            "(select ww.warehouse.id from WorkerWarehouse ww where ww.worker.id =:workerId and ww.role =:role)")
    Set<WorkerWarehouse> findWorkers(UUID workerId, Role role);

    @Query("SELECT  new com.example.awarehouse.module.warehouse.dto.GroupWarehouseDto(new com.example.awarehouse.module.group.dto.BasicGroupInfoDto(wg.id, wg.name), new com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto(ww.warehouse.id, ww.warehouse.name))" +
            " FROM WorkerWarehouse ww " +
            "JOIN ww.warehouse.warehouseGroups wg " +
            "WHERE wg IN (SELECT wg FROM WorkerWarehouse ww WHERE ww.warehouse.id = :warehouseId) " +
            "AND ww.worker.id = :workerId")
     List<GroupWarehouseDto> findWorkerWarehousesWithGroups(UUID warehouseId, UUID workerId);

    List<WorkerWarehouse> findByWorkerIdAndWarehouseIdIn(UUID workerId, Set<UUID> warehouseIds);

}
