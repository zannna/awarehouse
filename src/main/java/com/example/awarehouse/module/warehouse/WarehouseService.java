package com.example.awarehouse.module.warehouse;

import com.example.awarehouse.module.token.SharingToken;
import com.example.awarehouse.module.warehouse.dto.WarehouseCreation;
import com.example.awarehouse.module.warehouse.dto.WarehouseListResponseDto;
import com.example.awarehouse.module.warehouse.dto.WarehouseRequest;
import com.example.awarehouse.module.warehouse.dto.WarehouseResponseDto;
import com.example.awarehouse.module.warehouse.group.GroupRepository;
import com.example.awarehouse.module.warehouse.group.WarehouseGroup;
import com.example.awarehouse.module.warehouse.mapper.WarehouseMapper;
import com.example.awarehouse.module.token.SharingTokenService;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WarehouseService {
    WorkerWarehouseService workerWarehouseService;
    SharingTokenService sharingTokenService;
    WarehouseRepository warehouseRepository;
    GroupRepository groupRepository;
    Validator validator;


    public void updateWarehouse(WarehouseRequest warehouseRequest) {
        //  warehouseRepository.updateWarehous()
    }

    @Transactional
    public WarehouseResponseDto createWarehouse(WarehouseCreation warehouseCreation) {
        validator.validate(warehouseCreation);
        Warehouse warehouse = warehouseCreationToWarehouse(warehouseCreation);
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        setAdmin(savedWarehouse.getId());
        sharingTokenService.createSharingToken(savedWarehouse);
        return WarehouseMapper.toWarehouseResponseDto(savedWarehouse);
    }

    private void setAdmin(UUID warehouseId) {
        UUID workerId = workerId();
        workerWarehouseService.newRelation(warehouseId, workerId, Role.ADMIN);
    }

    private UUID workerId() {
        Jwt token = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UUID.fromString(token.getClaim("sub"));
    }

    private Warehouse warehouseCreationToWarehouse(WarehouseCreation warehouseCreation) {
        Set<WarehouseGroup> warehouseGroups = findGroups(warehouseCreation.groupIds());
        Warehouse warehouse = WarehouseMapper.toWarehouse(warehouseCreation, warehouseGroups);
        return warehouse;
    }

    private Set<WarehouseGroup> findGroups(Set<Long> groupsId) {
        return Optional.ofNullable(groupRepository.findAllById(groupsId)).orElseGet(Collections::emptyList).stream().collect(Collectors.toSet());
    }

    public List<WarehouseListResponseDto> getWarehouses() {
        UUID workerId = workerId();
        return workerWarehouseService.getWarehouses(workerId);
    }

}
