package com.example.awarehouse.module.product.dto;

import java.util.UUID;

public record ProductWarehouseDto(UUID productWarehouseId, String warehouseName, Integer row, Integer shelfNumber, Integer tierNumber, Double amount) {
}
