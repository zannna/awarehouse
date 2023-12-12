package com.example.awarehouse.module.warehouse.shelve.tier;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface ShelveTierRepository extends JpaRepository<ShelveTier, UUID>{

    Optional<ShelveTier> findByShelveWarehouseIdAndShelveNumberAndNumber(UUID warehouseId, int i, int i1);
}
