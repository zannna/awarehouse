package com.example.awarehouse.module.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class ShelfWithProductsDto {
    private UUID id;
    private int number;
    private String name;
    private Boolean hasFreeSpace;
    private List<TierWithProductsDto> tiers;
}
