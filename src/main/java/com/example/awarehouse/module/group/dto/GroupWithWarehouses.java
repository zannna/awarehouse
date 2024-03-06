package com.example.awarehouse.module.group.dto;

import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class GroupWithWarehouses {
    private BasicGroupInfoDto group;
    private List<BasicWarehouseInfoDto> warehouses;
}
