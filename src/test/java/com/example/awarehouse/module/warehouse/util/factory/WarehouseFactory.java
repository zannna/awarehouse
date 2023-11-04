package com.example.awarehouse.module.warehouse.util.factory;

import com.example.awarehouse.module.warehouse.LengthUnit;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.token.SharingToken;

import static com.example.awarehouse.module.warehouse.util.WarehouseConstants.WAREHOUSE_ID;
import static com.example.awarehouse.module.warehouse.util.factory.GroupFactory.createSetOfGroups;

public class WarehouseFactory {
    public static Warehouse createWarehouse() {
        return  new Warehouse(WAREHOUSE_ID, "name",  LengthUnit.METER, 3, createSetOfGroups(),
                null, new SharingToken());
    }
}
