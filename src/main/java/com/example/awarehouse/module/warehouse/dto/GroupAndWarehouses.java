package com.example.awarehouse.module.warehouse.dto;

import com.example.awarehouse.module.group.dto.GroupWithWarehouses;

import java.util.List;

public record GroupAndWarehouses(List<GroupWithWarehouses> groupWithWarehouses, List<BasicWarehouseInfoDto> warehousesWithoutGroup) {
}
