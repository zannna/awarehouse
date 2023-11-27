package com.example.awarehouse.module.warehouse;

import com.example.awarehouse.module.warehouse.dto.*;
import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.WarehouseGroupService;
import com.example.awarehouse.module.warehouse.mapper.WarehouseMapper;
import com.example.awarehouse.module.token.SharingTokenService;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.GroupNotExistException;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WarehouseNotExistException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.GROUP_NOT_EXIST;
import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.WAREHOUSE_NOT_EXIST;

@Service
@AllArgsConstructor
public class WarehouseService {
    private final WorkerWarehouseService workerWarehouseService;
    private final SharingTokenService sharingTokenService;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseGroupService groupService;
    private final Validator validator;


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
        Set<WarehouseGroup> warehouseGroups = groupService.getGroups(warehouseCreation.groupIds());
        Warehouse warehouse = WarehouseMapper.toWarehouse(warehouseCreation, warehouseGroups);
        return warehouse;
    }

    public List<BasicWarehouseInfoDto> getWarehouses() {
        UUID workerId = workerId();
        return workerWarehouseService.getWarehouses(workerId);
    }

    public void addWarehouseToGroup(UUID groupId, WarehouseIdDto warehouseIdDto) {
        Warehouse warehouse = getWarehouse(warehouseIdDto.id()).orElseThrow(()->new WarehouseNotExistException(WAREHOUSE_NOT_EXIST));
        WarehouseGroup group = groupService.getGroup(groupId).orElseThrow(()->new GroupNotExistException(GROUP_NOT_EXIST));
        warehouse.addGroup(group);
    }

    public Optional<Warehouse> getWarehouse(UUID warehouseId){
        return warehouseRepository.findById(warehouseId);
    }

}
