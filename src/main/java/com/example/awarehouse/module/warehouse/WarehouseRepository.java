package com.example.awarehouse.module.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {
//    @Query("UPDATE warehouse WHERE id= :id"
//    UUID upateWarehouse(UUID ownerId);
}
