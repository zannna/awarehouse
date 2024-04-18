package com.example.awarehouse.module.warehouse.group.util.factory;

import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;

import java.util.Set;
import java.util.UUID;

public class WarehouseGroupFactory {
    public static String createWarehouseGroupJson(){
        return """
                {
                    "name":"toys"
                }
                """;
    }
    public static BasicGroupInfoDto createGroupResponse(){
        return new BasicGroupInfoDto(UUID.fromString("c6e1b3aa-948d-11ee-b9d1-0242ac120002"),"toys", false);
    }

    public static Set<BasicGroupInfoDto> createSetOfGroup(){
        return Set.of(
                new BasicGroupInfoDto(UUID.fromString("c6e1b3aa-948d-11ee-b9d1-0242ac120002"),"Group 1", false),
                new BasicGroupInfoDto(UUID.fromString("d985e5e4-948d-11ee-b9d1-0242ac120002"),"Group 2", false),
                new BasicGroupInfoDto(UUID.fromString("e2802bdc-948d-11ee-b9d1-0242ac120002"),"Group 3", false)
        );
    }
    public static String warehouseGroupJson(){
        return """
                {
                    "id":"1b0cbc82-236f-4846-ac08-6d88baa91294"
                }
                """;
    }
}
