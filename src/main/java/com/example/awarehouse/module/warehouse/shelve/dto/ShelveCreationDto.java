package com.example.awarehouse.module.warehouse.shelve.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class ShelveCreationDto {
    @NotNull(message = "Number is mandatory")
    private int number;
    private String name;
    boolean size;
    private DimensionsDto dimensions;
    private boolean sameSizeTiers;
    private List<ShelveTierCreationDto> tiers;
}
