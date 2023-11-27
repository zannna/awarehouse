package com.example.awarehouse.module.warehouse.mapper;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import com.example.awarehouse.module.warehouse.dto.GroupResponseDto;
import com.example.awarehouse.module.warehouse.dto.WarehouseCreation;
import com.example.awarehouse.module.warehouse.dto.WarehouseResponseDto;

import java.util.Set;
import java.util.stream.Collectors;

public class WarehouseMapper {
    public static Warehouse toWarehouse(WarehouseCreation warehouseCreation, Set<WarehouseGroup> warehouseGroups){
        return new Warehouse(warehouseCreation.name(), warehouseCreation.unit(), warehouseCreation.numberOfRows(), warehouseGroups);
    }

    public static WarehouseResponseDto toWarehouseResponseDto(Warehouse warehouse){
        Set<GroupResponseDto> groups = warehouse.getWarehouseGroups().stream()
                .map(GroupMapper::toGroupResponseDto).collect(Collectors.toSet());
        return new WarehouseResponseDto(warehouse.getId(), warehouse.getName(), warehouse.getUnit().name(), warehouse.getRowsNumber(), groups);
    }
    public static BasicWarehouseInfoDto toBasicWarehouseInfoDto(Warehouse warehouse){
        if(warehouse == null)
            return null;
        return new BasicWarehouseInfoDto(warehouse.getId(), warehouse.getName());
    }
}
