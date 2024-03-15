package com.example.awarehouse.module.warehouse.shelve;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
interface ShelveRepository extends JpaRepository<Shelve, UUID> {
    Optional<Shelve> findByWarehouseIdAndNumber(UUID warehouseId, int number);

    List<Shelve> findAllByWarehouseIdOrderByNumber(UUID warehouseId);

    Page<Shelve> findAllByWarehouseIdOrderByRowAscNumberAsc(UUID warehouseId, Pageable pageable);

    Optional<Shelve> findByWarehouseIdAndRowAndNumber(UUID warehouseId, Integer row, Integer shelfNumber);
}
