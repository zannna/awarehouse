package com.example.awarehouse.module.warehouse.util.factory;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.warehouse.dto.GroupResponseDto;
import com.example.awarehouse.module.warehouse.mapper.GroupMapper;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class GroupFactory {

    public static Set<WarehouseGroup> createListOfGroups(){
        return Set.of(new WarehouseGroup(UUID.fromString("c3069688-9a91-11ee-b9d1-0242ac120002"), "clothes", null), new WarehouseGroup(UUID.fromString("c4158a86-9a94-11ee-b9d1-0242ac120002"),"toys", null));
    }
    public  static Set<WarehouseGroup> createSetOfGroups(){
        return createListOfGroups().stream().collect(java.util.stream.Collectors.toSet());
    }

    public  static Set<GroupResponseDto> createSetOfGroupResponseDto(Optional<Set<WarehouseGroup>> groups){
        return groups.orElseGet(Collections::emptySet).stream().map(GroupMapper :: toGroupResponseDto).collect(java.util.stream.Collectors.toSet());
    }


}
