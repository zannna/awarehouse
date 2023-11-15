package com.example.awarehouse.module.warehouse.group.util.factory;

import com.example.awarehouse.module.warehouse.group.dto.BasicGroupInfoDto;

import java.util.Set;

public class WarehouseGroupFactory {
    public static String createWarehouseGroupJson(){
        return """
                {
                    "name":"toys"
                }
                """;
    }
    public static BasicGroupInfoDto createGroupResponse(){
        return new BasicGroupInfoDto(1L,"toys");
    }

    public static Set<BasicGroupInfoDto> createSetOfGroup(){
        return Set.of(
                new BasicGroupInfoDto(1L,"Group 1"),
                new BasicGroupInfoDto(2L,"Group 2"),
                new BasicGroupInfoDto(3L,"Group 3")
        );
    }
}
