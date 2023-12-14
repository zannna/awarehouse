package com.example.awarehouse.module.warehouse.shelve;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface ShelveRepository extends JpaRepository<Shelve, Long> {
    Optional<Shelve> findByWarehouseIdAndNumber(UUID warehouseId, int number);
}
