package com.example.awarehouse.module.warehouse.shelve.dto;

import com.example.awarehouse.module.warehouse.util.unit.UnitEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;


public record DimensionsDto (
    double height,
   double width,
   double length,
   String unit){
}
