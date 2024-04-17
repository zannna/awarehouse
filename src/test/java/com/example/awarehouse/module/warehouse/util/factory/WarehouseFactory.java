package com.example.awarehouse.module.warehouse.util.factory;

import com.example.awarehouse.module.token.SharingToken;
import com.example.awarehouse.module.warehouse.util.unit.LengthUnit;
import com.example.awarehouse.module.warehouse.Warehouse;

import java.util.HashSet;

import static com.example.awarehouse.module.warehouse.util.WarehouseTestConstants.WAREHOUSE_ID;
import static com.example.awarehouse.module.warehouse.util.WarehouseTestConstants.WAREHOUSE_ID_2;
import static com.example.awarehouse.module.warehouse.util.factory.GroupFactory.createSetOfGroups;

public class WarehouseFactory {
    public static Warehouse createWarehouse() {
        return  Warehouse.builder()
                .id(WAREHOUSE_ID)
                .name("name")
                .unit(LengthUnit.METER)
                .rowsNumber(3)
                .warehouseGroups(createSetOfGroups()).
             build();
    }
    public static Warehouse createSecondWarehouse() {
        return Warehouse.builder()
                .id(WAREHOUSE_ID_2)
                .name("name 2")
                .unit(LengthUnit.METER)
                .rowsNumber(2)
                .build();
    }

}
