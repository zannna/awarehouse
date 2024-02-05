package com.example.awarehouse.module.group.mapper;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.group.dto.GroupWithWarehouses;
import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WarehouseGroupMapper {
    public static BasicGroupInfoDto toDto(WarehouseGroup warehouseGroup){
        if(warehouseGroup == null)
            return null;
        return new BasicGroupInfoDto(warehouseGroup.getId(), warehouseGroup.getName());
    }

    public static List<GroupWithWarehouses> toGroupWithWarehouses(Map<BasicGroupInfoDto, List<BasicWarehouseInfoDto>> map){
        return map.entrySet().stream()
                .map(entry -> new GroupWithWarehouses(entry.getKey().name(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
