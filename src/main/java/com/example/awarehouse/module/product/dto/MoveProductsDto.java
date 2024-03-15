package com.example.awarehouse.module.product.dto;

import com.example.awarehouse.module.product.ProductWarehouse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @Valid
    private Set<ProductWarehouseMoveInfo> productWarehouseMoveInfos;
    @NotNull(message = "Warehouse id cannot be null")
    private UUID warehouseId;
    @NotNull(message = "Shelf number cannot be null")
    private Integer shelfNumber;
    @NotNull(message = "Tier number cannot be null")
    private Integer tierNumber;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ProductWarehouseMoveInfo {
        @NotNull(message = "Product warehouse id cannot be null")
        private UUID productWarehouseId;
        @NotNull(message = "Amount cannot be null")
        @DecimalMin(value = "1.0", message = "Amount must be greater than 0")
        private Double amount;
    }
}