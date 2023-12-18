package com.example.awarehouse.module.product.dto;

import java.util.UUID;

public record ProductWithWarehouseDto (UUID id, String title, PriceDto price, String warehouseName){
}
