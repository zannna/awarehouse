package com.example.awarehouse.module.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SharingTokenRepository extends JpaRepository<SharingToken, Long> {
    Optional<SharingToken> findByWarehouseId(UUID warehouseId);
    Optional<SharingToken> findBySharingToken(String sharingToken);

}
