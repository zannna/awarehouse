package com.example.awarehouse.module.warehouse.group.util.factory;

import com.example.awarehouse.module.warehouse.group.dto.GroupResponse;

public class WarehouseGroupFactory {
    public static String createWarehouseGroupJson(){
        return """
                {
                    "name":"toys"
                }
                """;
    }
    public static  GroupResponse createGroupResponse(){
        return new GroupResponse(1L,"toys");
    }
}
