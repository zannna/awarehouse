package com.example.awarehouse.module.warehouse.shelve.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class ShelfCreationDto {
    @NotNull(message = "Number is mandatory")
    private int number;
    private String name;
    boolean size;
    private DimensionsDto dimensions;
    private double shelfUnit;
    private boolean sameSizeTiers;
    private int row;
    private List<ShelveTierCreationDto> tiers;
}
