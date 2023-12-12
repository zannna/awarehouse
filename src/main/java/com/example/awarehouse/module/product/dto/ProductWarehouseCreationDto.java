package com.example.awarehouse.module.product.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductWarehouseCreationDto(@NotNull(message = "Warehouse Id must be provided") UUID warehouseId, Integer shelveNumber, Integer tierNumber, @NotNull(message = "WarehouseId must be provided") Double amount) {

}
