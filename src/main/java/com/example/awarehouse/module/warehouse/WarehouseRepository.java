package com.example.awarehouse.module.warehouse;

import com.example.awarehouse.module.group.WarehouseGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {
   // List<Warehouse> findAllByWarehouseGroupsInAndWorkerWarehousesWorkerId(UUID workerId, List<WarehouseGroup> groups);
//    @Query("UPDATE warehouse WHERE id= :id"
//    UUID upateWarehouse(UUID ownerId);
}
