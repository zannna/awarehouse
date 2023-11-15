package com.example.awarehouse.module.warehouse.util.factory;

import com.example.awarehouse.module.token.SharingToken;
import com.example.awarehouse.module.warehouse.LengthUnit;
import com.example.awarehouse.module.warehouse.Warehouse;

import java.util.HashSet;

import static com.example.awarehouse.module.warehouse.util.WarehouseTestConstants.WAREHOUSE_ID;
import static com.example.awarehouse.module.warehouse.util.WarehouseTestConstants.WAREHOUSE_ID_2;
import static com.example.awarehouse.module.warehouse.util.factory.GroupFactory.createSetOfGroups;

public class WarehouseFactory {
    public static Warehouse createWarehouse() {
        return  new Warehouse(WAREHOUSE_ID, "name",  LengthUnit.METER, 3, createSetOfGroups(),
                null, new SharingToken());
    }
    public static Warehouse createSecondWarehouse() {
        return  new Warehouse(WAREHOUSE_ID_2, "name2",  LengthUnit.METER, 3, new HashSet<>(),
                new HashSet<>(), new SharingToken());
    }

}
