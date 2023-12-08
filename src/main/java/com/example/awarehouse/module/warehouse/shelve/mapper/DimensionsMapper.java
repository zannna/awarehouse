package com.example.awarehouse.module.warehouse.shelve.mapper;

import com.example.awarehouse.module.warehouse.shelve.Dimensions;
import com.example.awarehouse.module.warehouse.shelve.dto.DimensionsDto;

public class DimensionsMapper {

        public static Dimensions toDimensions(DimensionsDto dimensionsDto) {

            return new Dimensions(dimensionsDto.height(), dimensionsDto.width(), dimensionsDto.length());
        }

    public static DimensionsDto toDto(Dimensions dimensions) {
        return new DimensionsDto(dimensions.getHeight(), dimensions.getWidth(), dimensions.getLength());
    }
}
