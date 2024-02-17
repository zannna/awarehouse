package com.example.awarehouse.module.warehouse.util.factory;

import com.example.awarehouse.module.warehouse.util.unit.LengthUnit;
import com.example.awarehouse.module.warehouse.dto.WarehouseCreation;

import java.util.Set;
import java.util.UUID;

public class WarehouseDtoFactory {

    public static WarehouseCreation createWarehouseCreation() {
        return new WarehouseCreation("name",  LengthUnit.METER, 3, Set.of(UUID.fromString(""), UUID.fromString("")));
    }


}
