package com.example.awarehouse.module.product.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductWarehouseDto(UUID productWarehouseId, String warehouseName, Integer shelveNumber, Integer tierNumber,Double amount) {
}
