package com.example.awarehouse.module.warehouse.shelve.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class ShelveCreationDto {
    private int number;
    private String name;
    boolean size;
    private double height;
    private double width;
    private double length;
    private int numberOfTiers;
    private boolean sameSizeTiers;
    private List<ShelveTierCreationDto> tiers;
}
