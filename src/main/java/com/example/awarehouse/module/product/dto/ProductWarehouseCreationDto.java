package com.example.awarehouse.module.product.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductWarehouseCreationDto(@NotNull(message = "Warehouse Id must be provided") UUID warehouseId, String warehouseName,Integer row, Integer shelfNumber, Integer tierNumber, @NotNull(message = "WarehouseId must be provided") Double amount) {

}
