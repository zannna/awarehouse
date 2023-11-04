package com.example.awarehouse.module.warehouse.util.factory;

import com.example.awarehouse.module.warehouse.group.WarehouseGroup;
import com.example.awarehouse.module.warehouse.dto.GroupResponseDto;
import com.example.awarehouse.module.warehouse.mapper.GroupMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GroupFactory {

    public static List<WarehouseGroup> createListOfGroups(){
        return List.of(new WarehouseGroup(1L, "clothes"), new WarehouseGroup(2L,"toys"));
    }
    public  static Set<WarehouseGroup> createSetOfGroups(){
        return createListOfGroups().stream().collect(java.util.stream.Collectors.toSet());
    }

    public  static Set<GroupResponseDto> createSetOfGroupResponseDto(Optional<List<WarehouseGroup>> groups){
        return groups.orElseGet(Collections::emptyList).stream().map(GroupMapper :: toGroupResponseDto).collect(java.util.stream.Collectors.toSet());
    }


}
