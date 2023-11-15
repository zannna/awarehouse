package com.example.awarehouse.module.warehouse.group;

import com.example.awarehouse.module.warehouse.group.dto.BasicGroupInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<WarehouseGroup, Long> {
@Query("SELECT COUNT (id) > 0 FROM WarehouseGroup WHERE name = :name AND worker.id = :workerId")
   boolean checkIfNameExists(String name, UUID workerId);

@Query(value = "INSERT INTO warehouse_group (name, worker_id) VALUES(:name, :workerId) RETURNING  *", nativeQuery = true)
WarehouseGroup createGroup(String name, UUID workerId);

Set<BasicGroupInfoDto> findByWorkerId(UUID workerId);
}
