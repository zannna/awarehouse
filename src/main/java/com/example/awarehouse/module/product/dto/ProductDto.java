package com.example.awarehouse.module.product.dto;

import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.product.Price;
import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProductDto {

    private UUID id;
    private String title;
    private double amount;
    private PriceDto price;
    private String photo;
    private BasicGroupInfoDto group;
    private List<ProductWarehouseDto> productWarehouses;


}
