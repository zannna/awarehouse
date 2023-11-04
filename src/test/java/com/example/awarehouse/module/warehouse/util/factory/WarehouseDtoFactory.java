package com.example.awarehouse.module.warehouse.util.factory;

import com.example.awarehouse.module.warehouse.LengthUnit;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.dto.WarehouseCreation;

import java.util.Set;

import static com.example.awarehouse.module.warehouse.util.factory.GroupFactory.createSetOfGroups;

public class WarehouseDtoFactory {

    public static WarehouseCreation createWarehouseCreation() {
        return new WarehouseCreation("name",  LengthUnit.METER, 3, Set.of(1L, 2L));
    }


}
