package com.example.awarehouse.module.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FilterDto {
    private  Map<String, String> sortConditions;
    private  Map<String, String> searchConditions;
    private  List<UUID> warehouseIds;
    private List<UUID> groupIds;
}
