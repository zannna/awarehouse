package com.example.awarehouse.module.warehouse.dto;

import com.example.awarehouse.module.warehouse.LengthUnit;

public record WarehouseRequest(LengthUnit lengthUnit, int numberOfRows){
}
