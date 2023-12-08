package com.example.awarehouse.module.warehouse.shelve.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ShelveDto {
    private UUID id;
    private int number;
    private String name;
    private boolean size;
    private boolean sameSizeTiers;
    private DimensionsDto dimensions;
    private Set<ShelveTierDto> shelveTiers;
}
