package com.example.awarehouse.module.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProductsDto {
    private List<UUID> productIds;
    private List<UUID> productWarehouseIds;

}
