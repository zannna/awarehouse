package com.example.awarehouse.module.warehouse.dto;

import java.util.Set;
import java.util.UUID;

public record WarehouseResponseDto (UUID id, String name, String unit, int rowsNumber, Set<GroupResponseDto> groups){
}
