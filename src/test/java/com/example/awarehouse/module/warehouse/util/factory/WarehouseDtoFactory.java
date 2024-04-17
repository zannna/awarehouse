package com.example.awarehouse.module.warehouse.util.factory;

import com.example.awarehouse.module.warehouse.util.unit.LengthUnit;
import com.example.awarehouse.module.warehouse.dto.WarehouseCreation;

import java.util.Set;
import java.util.UUID;

public class WarehouseDtoFactory {

    public static WarehouseCreation createWarehouseResponse() {
        return new WarehouseCreation("name",  LengthUnit.METER, 3, Set.of(UUID.fromString(""), UUID.fromString("")));
    }


    public static WarehouseCreation createWarehouseCreation() {
        return new WarehouseCreation("name",  LengthUnit.METER, 3, Set.of(UUID.fromString("0c9fdb32-14ea-4337-bfca-a30fa309b2d5"),
                UUID.fromString("2fb03da2-6ba9-4239-a5bf-6dd6e91a6802")));
    }
}
