package com.example.awarehouse.module.warehouse.mapper;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.warehouse.dto.GroupResponseDto;

public class GroupMapper {

    public static GroupResponseDto toGroupResponseDto(WarehouseGroup warehouseGroup){
        return new GroupResponseDto(warehouseGroup.getId(), warehouseGroup.getName());
    }

    public static BasicGroupInfoDto toBasicGroupInfoDto(WarehouseGroup warehouseGroup){
        return new BasicGroupInfoDto(warehouseGroup.getId(), warehouseGroup.getName(), null);
    }
}
