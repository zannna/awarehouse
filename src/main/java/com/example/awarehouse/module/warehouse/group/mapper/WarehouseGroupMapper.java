package com.example.awarehouse.module.warehouse.group.mapper;

import com.example.awarehouse.module.warehouse.group.WarehouseGroup;
import com.example.awarehouse.module.warehouse.group.dto.GroupResponse;

public class WarehouseGroupMapper {
    public static GroupResponse toDto(WarehouseGroup warehouseGroup){
        return new GroupResponse(warehouseGroup.getId(), warehouseGroup.getName());
    }
}
