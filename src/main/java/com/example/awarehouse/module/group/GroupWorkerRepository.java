package com.example.awarehouse.module.group;

import com.example.awarehouse.module.warehouse.Role;

import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface GroupWorkerRepository extends  JpaRepository<GroupWorker, UUID> {
    @Query("select distinct gw.group from GroupWorker  gw where gw.worker.id =:workerId and gw.role =:role")
    Set<WarehouseGroup> findGroupWhereWorkerHasRole(UUID workerId, Role role);

    @Query("select distinct gw from GroupWorker gw where gw.group.id in " +
            "(select gw.group.id from GroupWorker gw  where gw.worker.id =:workerId and gw.role =:role)")
    Set<GroupWorker> findWorkers(UUID workerId, Role role);

    Optional<GroupWorker> findByGroupIdAndWorkerId(UUID groupId, UUID workerId);

    @Query("select distinct gw from GroupWorker gw where gw.worker.id = :workerId and gw.group.id in :groupIds")
    List<GroupWorker> findAllDistinctByWorkerIdAndGroupIdIn(UUID workerId, Set<UUID> groupIds);

    @Query("select distinct gw.group from GroupWorker gw where gw.worker.id = :workerId")
    List<WarehouseGroup> findWorkerGroups(UUID workerId);

    Optional<GroupWorker> findByWorkerIdAndGroupName(UUID userId, String group);
}

