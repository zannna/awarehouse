package com.example.awarehouse.module.warehouse.shelve.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class BasicShelveTierInfoDto {
    private UUID id;
    private int number;
    private String name;
    private  double occupiedVolume;
}
