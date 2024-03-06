package com.example.awarehouse.module.product.dto;

import com.example.awarehouse.module.product.ProductWarehouse;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MoveProductsDto {
    private Set<ProductWarehouseMoveInfo> productWarehouseMoveInfos;
    private UUID warehouseId;
    private Integer shelfNumber;
    private Integer tierNumber;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ProductWarehouseMoveInfo {
        private UUID productWarehouseId;
        private Double amount;
    }
}