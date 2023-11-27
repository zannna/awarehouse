package com.example.awarehouse.module.product.dto;

import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.product.Price;
import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Builder
public class ProductDTO {

    private UUID id;
    private String title;
    private double amount;
    private Price price;
    private String photo;

    private BasicGroupInfoDto group;
    private Set<BasicWarehouseInfoDto> productWarehouses;


}
