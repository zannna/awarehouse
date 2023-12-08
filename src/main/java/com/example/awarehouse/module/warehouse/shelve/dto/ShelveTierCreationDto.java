package com.example.awarehouse.module.warehouse.shelve.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ShelveTierCreationDto {
    private int number;
    private String name;
    boolean size;
    private double height;
    private double width;
    private double length;
}
