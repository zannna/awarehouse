package com.example.awarehouse.module.warehouse.factory;

import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.group.WarehouseGroup;

import java.util.Set;
import java.util.UUID;

public class WarehouseFactory {
    public static Set<Warehouse> getSetOfWarehouses() {
        return Set.of(
                Warehouse.builder().name("Warehouse 1").id(UUID.fromString("5d8a8b84-8227-11ee-b962-0242ac120002"))
                        .warehouseGroups(Set.of(new WarehouseGroup(1L, "Group 1"), new WarehouseGroup(2L, "Group 2"))).build(),
                Warehouse.builder().name("Warehouse 2").id(UUID.fromString("835ec186-8227-11ee-b962-0242ac120002")).build(),
                Warehouse.builder().name("Warehouse 3").id(UUID.fromString("8c6e5ac0-8227-11ee-b962-0242ac120002"))
                        .warehouseGroups(Set.of(new WarehouseGroup(2L, "Group 2"))).build()
        );
    }
}
