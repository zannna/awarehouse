package com.example.awarehouse.module.warehouse.shelve.tier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface ShelveTierRepository extends JpaRepository<ShelveTier, UUID>{

    Optional<ShelveTier> findByShelveWarehouseIdAndShelveNumberAndNumber(UUID warehouseId, int i, int i1);
}
