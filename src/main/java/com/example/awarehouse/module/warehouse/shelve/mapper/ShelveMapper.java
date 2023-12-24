package com.example.awarehouse.module.warehouse.shelve.mapper;

import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.shelve.Dimensions;
import com.example.awarehouse.module.warehouse.shelve.Shelve;
import com.example.awarehouse.module.warehouse.shelve.dto.DimensionsDto;
import com.example.awarehouse.module.warehouse.shelve.dto.ShelveCreationDto;
import com.example.awarehouse.module.warehouse.shelve.dto.ShelveDto;
import com.example.awarehouse.module.warehouse.shelve.dto.ShelveTierDto;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ShelveMapper {

    public static Shelve toShelve(ShelveCreationDto shelveDto, Warehouse warehouse) {
        Dimensions dimensions =DimensionsMapper.toDimensions(shelveDto.getDimensions());
        Set<ShelveTier> tiers = ShelveTierMapper.toTierSet(shelveDto.getTiers());
        return new Shelve(shelveDto.getNumber(), shelveDto.getName(),  dimensions, shelveDto.isSize(), tiers, warehouse);
    }

    public static ShelveDto toShelveDto(Shelve shelve) {
        DimensionsDto dimensions =DimensionsMapper.toDto(shelve.getDimensions());
       List<ShelveTierDto> tiers = ShelveTierMapper.toTierDtoList(shelve.getShelveTiers());
       Boolean freeSpace=null;
        if(tiers!=null) {
           freeSpace = tiers.stream().filter(t -> t.getFillPercentage() < 100).findFirst().isPresent();
       }
        return new ShelveDto(shelve.getId(), shelve.getNumber(), shelve.getName(),shelve.isSize(), dimensions, tiers, freeSpace);
    }

    public static List<ShelveDto> toShelveDtos(List<Shelve> shelves) {
        return shelves.stream().map(ShelveMapper::toShelveDto).sorted(Comparator.comparing(ShelveDto::getNumber)).toList();
    }
}
