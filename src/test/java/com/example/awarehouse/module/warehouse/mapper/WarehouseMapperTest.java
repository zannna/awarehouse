package com.example.awarehouse.module.warehouse.mapper;

import com.example.awarehouse.module.warehouse.group.WarehouseGroup;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.dto.WarehouseCreation;
import com.example.awarehouse.module.warehouse.dto.WarehouseResponseDto;
import com.example.awarehouse.module.warehouse.util.factory.GroupFactory;
import com.example.awarehouse.module.warehouse.util.factory.WarehouseDtoFactory;
import com.example.awarehouse.module.warehouse.util.factory.WarehouseFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class WarehouseMapperTest {

    @Test
    void toWarehouse_whenWarehouseCreationIsValid_thenWarehouseIsMapped() {
        //given
        WarehouseCreation warehouseCreation = WarehouseDtoFactory.createWarehouseCreation();
        Set<WarehouseGroup> warehouseGroups = GroupFactory.createSetOfGroups();

        //when
        Warehouse warehouse = WarehouseMapper.toWarehouse(warehouseCreation, warehouseGroups);

        //then
        assertThat(warehouse.getName()).isEqualTo(warehouseCreation.name());
        assertThat(warehouse.getUnit()).isEqualTo(warehouseCreation.unit());
        assertThat(warehouse.getRowsNumber()).isEqualTo(warehouseCreation.numberOfRows());
        assertThat(warehouse.getWarehouseGroups()).isEqualTo(warehouseGroups);
    }

    @Test
    void toWarehouseResponseDto_whenWarehouseIsValid_thenWarehouseResponseDtoIsMapped() {
        //given
        Warehouse warehouse = WarehouseFactory.createWarehouse();

        //when
        WarehouseResponseDto warehouseResponseDto = WarehouseMapper.toWarehouseResponseDto(warehouse);

        //then
        assertThat(warehouseResponseDto.id()).isEqualTo(warehouse.getId());
        assertThat(warehouseResponseDto.name()).isEqualTo(warehouse.getName());
        assertThat(warehouseResponseDto.unit()).isEqualTo(warehouse.getUnit().name());
        assertThat(warehouseResponseDto.rowsNumber()).isEqualTo(warehouse.getRowsNumber());
        assertThat(warehouseResponseDto.groups()).isEqualTo(warehouse.getWarehouseGroups().stream().map(GroupMapper::toGroupResponseDto).collect(Collectors.toSet()));
    }

}
