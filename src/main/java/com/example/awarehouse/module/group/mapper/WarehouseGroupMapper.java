package com.example.awarehouse.module.group.mapper;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;

public class WarehouseGroupMapper {
    public static BasicGroupInfoDto toDto(WarehouseGroup warehouseGroup){
        if(warehouseGroup == null)
            return null;
        return new BasicGroupInfoDto(warehouseGroup.getId(), warehouseGroup.getName());
    }
}
