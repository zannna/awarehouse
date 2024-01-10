package com.example.awarehouse.module.product.dto;

import java.util.List;
import java.util.UUID;

public record ProductFreePlaceDto(Double height, Double width, Double length, Double amount, List<UUID> warehouseIds){
}
