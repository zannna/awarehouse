package com.example.awarehouse.module.warehouse.shelve.mapper;

import com.example.awarehouse.module.warehouse.shelve.Dimensions;
import com.example.awarehouse.module.warehouse.shelve.dto.DimensionsDto;
import com.example.awarehouse.module.warehouse.util.unit.LengthUnit;
import com.example.awarehouse.module.warehouse.util.unit.MetricConverter;
import com.example.awarehouse.module.warehouse.util.unit.MetricConverterProvider;
import com.example.awarehouse.module.warehouse.util.unit.UnitEnum;

public class DimensionsMapper {

        public static Dimensions toDimensions(DimensionsDto dimensionsDto) {

            return new Dimensions(dimensionsDto.height(), dimensionsDto.width(), dimensionsDto.length());
        }
    public static Dimensions toDimensions(DimensionsDto dimensionsDto,LengthUnit lengthUnit){
         MetricConverter metricConverter = MetricConverterProvider.getConverter(lengthUnit);
         String unit = dimensionsDto.unit();
        return new Dimensions( metricConverter.convert(unit, dimensionsDto.height()), metricConverter.convert(unit,dimensionsDto.width()),
                metricConverter.convert(unit,dimensionsDto.length()));
    }

    public static DimensionsDto toDto(Dimensions dimensions) {
        return new DimensionsDto(dimensions.getHeight(), dimensions.getWidth(), dimensions.getLength(), null);
    }
}
