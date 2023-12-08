package com.example.awarehouse.module.warehouse.shelve.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


public record DimensionsDto (
    double height,
   double width,
   double length){
}
