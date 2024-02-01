package com.example.awarehouse.module.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface WorkerRepository extends JpaRepository<Worker, UUID> {

    @Query(nativeQuery = true, value = "SELECT worker.* FROM worker join worker_warehouse ww on worker.worker_id = ww.worker_id WHERE ww.worker_id = ?1 AND ww.role = ?2")
    Optional<Worker> findWorkerWithConcreteIdAndRole(UUID uuid, String role);
}
