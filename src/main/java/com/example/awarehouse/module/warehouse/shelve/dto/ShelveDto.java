package com.example.awarehouse.module.warehouse.shelve.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ShelveDto {
    private UUID id;
    private int number;
    private String name;
    private boolean size;
    private DimensionsDto dimensions;
    private List<ShelveTierDto> tiers;
    private Boolean hasFreeSpace;
}
