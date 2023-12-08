package com.example.awarehouse.module.warehouse.shelve.mapper;

import com.example.awarehouse.module.product.dto.ProductDTO;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.shelve.Dimensions;
import com.example.awarehouse.module.warehouse.shelve.Shelve;
import com.example.awarehouse.module.warehouse.shelve.dto.DimensionsDto;
import com.example.awarehouse.module.warehouse.shelve.dto.ShelveCreationDto;
import com.example.awarehouse.module.warehouse.shelve.dto.ShelveDto;
import com.example.awarehouse.module.warehouse.shelve.dto.ShelveTierDto;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;

import java.util.List;
import java.util.Set;

public class ShelveMapper {

    public static Shelve toShelve(ShelveCreationDto shelveDto, Warehouse warehouse) {
        Dimensions dimensions =DimensionsMapper.toDimensions(shelveDto.getDimensions());
        Set<ShelveTier> tiers = ShelveTierMapper.toTierSet(shelveDto.getTiers());
        return new Shelve(shelveDto.getNumber(), shelveDto.getName(),  dimensions, shelveDto.isSize(), shelveDto.isSameSizeTiers(), tiers, warehouse);
    }

    public static ShelveDto toShelveDto(Shelve shelve) {
        DimensionsDto dimensions =DimensionsMapper.toDto(shelve.getDimensions());
        Set<ShelveTierDto> tiers = ShelveTierMapper.toTierDtoSet(shelve.getShelveTiers());
        return new ShelveDto(shelve.getId(), shelve.getNumber(), shelve.getName(),shelve.isSize(),  shelve.isSameSizeTiers(), dimensions, tiers);
    }
}
