package com.example.awarehouse.module.group;

import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WorkerWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;
import java.util.UUID;

public interface GroupWorkerRepository extends  JpaRepository<GroupWorker, UUID> {
    @Query("select distinct gw.group from GroupWorker  gw where gw.worker.id =:workerId and gw.role =:role")
    Set<WarehouseGroup> findGroupWhereWorkerHasRole(UUID workerId, Role role);

    @Query("select distinct gw from GroupWorker gw where gw.group.id in " +
            "(select gw.group.id from GroupWorker gw  where gw.worker.id =:workerId and gw.role =:role)")
    Set<GroupWorker> findWorkers(UUID workerId, Role role);

}
