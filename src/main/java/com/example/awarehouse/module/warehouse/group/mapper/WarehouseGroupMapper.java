package com.example.awarehouse.module.warehouse.group.mapper;

import com.example.awarehouse.module.warehouse.group.WarehouseGroup;
import com.example.awarehouse.module.warehouse.group.dto.BasicGroupInfoDto;

public class WarehouseGroupMapper {
    public static BasicGroupInfoDto toDto(WarehouseGroup warehouseGroup){
        return new BasicGroupInfoDto(warehouseGroup.getId(), warehouseGroup.getName());
    }
}
