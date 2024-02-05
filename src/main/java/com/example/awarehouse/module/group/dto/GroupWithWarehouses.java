package com.example.awarehouse.module.group.dto;

import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;

import java.util.List;

public record GroupWithWarehouses(String groupName, List<BasicWarehouseInfoDto> warehouses) {

}
