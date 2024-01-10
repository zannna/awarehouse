package com.example.awarehouse.module.product.dto;

import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.product.Price;
import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import com.example.awarehouse.module.warehouse.shelve.Dimensions;
import com.example.awarehouse.module.warehouse.shelve.dto.DimensionsDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;
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
    private double amount;
    private PriceDto price;
    private String photo;
    private DimensionsDto dimensionsDto;
    private BasicGroupInfoDto group;
    private List<ProductWarehouseDto> productWarehouses;

}
