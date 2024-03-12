package com.example.awarehouse.module.product.dto;

import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.warehouse.shelve.dto.DimensionsDto;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProductDto {

    private UUID id;
    private String title;
    private double amountGroup;
    private PriceDto price;
    private BasicGroupInfoDto group;
    private List<ProductWarehouseDto> productWarehouses;
    private DimensionsDto dimensions;
    private String image;

}
