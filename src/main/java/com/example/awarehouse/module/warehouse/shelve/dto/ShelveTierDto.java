package com.example.awarehouse.module.warehouse.shelve.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ShelveTierDto {
    private UUID id;
    private int number;
    private String name;
    boolean size;
    private DimensionsDto dimensions;
    private  double fillPercentage;
}
