package com.example.awarehouse.module.warehouse;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.token.SharingToken;
import com.example.awarehouse.module.warehouse.util.unit.LengthUnit;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {

    @Id
    @Column(name = "warehouse_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private LengthUnit unit = LengthUnit.METER;

    private int rowsNumber = 0;

    @ManyToMany
    @JoinTable(name = "group_warehouse", joinColumns = {@JoinColumn(name = "warehouse_id")}, inverseJoinColumns = {@JoinColumn(name = "group_id")})
    private Set<WarehouseGroup> warehouseGroups = new HashSet<>();

    @OneToMany(mappedBy = "warehouse")
    private Set<WorkerWarehouse> workerWarehouses = new HashSet<>();


    public Warehouse(String name, LengthUnit unit, int rowsNumber, Set<WarehouseGroup> warehouseGroups) {
        this.name = name;
        this.unit = unit;
        this.rowsNumber = rowsNumber;
        this.warehouseGroups = warehouseGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse that = (Warehouse) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void addGroup(WarehouseGroup group){
        warehouseGroups.add(group);
    }

    public void setRowsNumber(int rowsNumber) {
        this.rowsNumber = rowsNumber;
    }
}
