package com.example.awarehouse.module.warehouse.shelve.mapper;

import com.example.awarehouse.module.warehouse.shelve.Dimensions;
import com.example.awarehouse.module.warehouse.shelve.dto.BasicShelveTierInfoDto;
import com.example.awarehouse.module.warehouse.shelve.dto.ShelveTierCreationDto;
import com.example.awarehouse.module.warehouse.shelve.dto.ShelveTierDto;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import com.example.awarehouse.module.warehouse.util.unit.LengthUnit;
import com.example.awarehouse.module.warehouse.util.unit.MetricConverter;
import com.example.awarehouse.module.warehouse.util.unit.MetricConverterProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ShelveTierMapper {

    public static Set<ShelveTier> toTierCreationSet(List<ShelveTierCreationDto> tierDtos, LengthUnit unit){
        return tierDtos.stream().map((t)->toTier(t, unit)).collect(Collectors.toSet());
    }
        public  static Set<ShelveTier> toTierSet(List<ShelveTierDto> tierDtos, LengthUnit unit){
        return tierDtos.stream().map((t)->toTier(t, unit)).collect(Collectors.toSet());
    }
    public static ShelveTier toTier(ShelveTierDto tierDto, LengthUnit lengthUnit) {
        Dimensions dimensions = DimensionsMapper.toDimensions(tierDto.getDimensions(),lengthUnit);
        return new ShelveTier(tierDto.getId(), tierDto.getNumber(), tierDto.getName(), tierDto.isSize(), dimensions);
    }

    public static ShelveTier toTier(ShelveTierCreationDto tierDto, LengthUnit lengthUnit) {;
        Dimensions dimensions = DimensionsMapper.toDimensions(tierDto.getDimensions(),lengthUnit);
        return new ShelveTier(tierDto.getNumber(), tierDto.getName(), tierDto.isSize(), dimensions);
    }

    public static List<ShelveTierDto> toTierDtoList(Set<ShelveTier> shelveTiers) {
        if(shelveTiers==null || shelveTiers.isEmpty())
            return new ArrayList<>();
        return shelveTiers.stream().map(ShelveTierMapper::toTierDto).sorted(
                Comparator.comparing(ShelveTierDto::getNumber)
        ).collect(Collectors.toList());
    }

    private static ShelveTierDto toTierDto(ShelveTier shelveTier) {
        return new ShelveTierDto(shelveTier.getId(), shelveTier.getNumber(), shelveTier.getName(), shelveTier.isSize(), DimensionsMapper.toDto(shelveTier.getDimensions()), shelveTier.getOccupiedVolume());
    }

    public static BasicShelveTierInfoDto toBasicShelveTierInfoDto(ShelveTier shelveTier) {
        return new BasicShelveTierInfoDto(shelveTier.getId(), shelveTier.getNumber(), shelveTier.getName(), shelveTier.getOccupiedVolume());
    }
}
