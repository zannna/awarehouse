package com.example.awarehouse.module.warehouse.shelve.tier;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
interface ShelveTierRepository extends JpaRepository<ShelveTier, UUID>{

    Optional<ShelveTier> findByShelveWarehouseIdAndShelveNumberAndNumber(UUID warehouseId, int i, int i1);

    @Query("SELECT t FROM ShelveTier t WHERE (t.occupiedVolume + :volume < t.dimensions.height * t.dimensions.width * t.dimensions.length " +
            "OR t.size = false ) AND t.shelve.warehouse.id IN :warehouseIds")
    List<ShelveTier> findFreeTiers(double volume, List<UUID> warehouseIds);

    @Transactional
    @Modifying
    @Query("DELETE FROM ShelveTier t WHERE t.id = :tierId")
    int deleteTier(UUID tierId);

    List<ShelveTier> findByShelveWarehouseIdOrderByShelveIdAscNumberAsc(UUID warehouseId);
}
