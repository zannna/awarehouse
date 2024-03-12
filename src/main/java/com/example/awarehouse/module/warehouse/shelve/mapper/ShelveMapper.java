package com.example.awarehouse.module.warehouse.shelve.mapper;

import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.shelve.Dimensions;
import com.example.awarehouse.module.warehouse.shelve.Shelve;
import com.example.awarehouse.module.warehouse.shelve.dto.*;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;

import java.util.*;

public class ShelveMapper {

    public static Shelve toShelve(ShelfCreationDto shelveDto, Warehouse warehouse) {
        Dimensions dimensions =DimensionsMapper.toDimensions(shelveDto.getDimensions(), warehouse.getUnit());
        Set<ShelveTier> tiers = ShelveTierMapper.toTierCreationSet(shelveDto.getTiers(), warehouse.getUnit());
        return new Shelve(shelveDto.getNumber(), shelveDto.getName(),  dimensions, shelveDto.isSize(), tiers, warehouse, shelveDto.getRow());
    }

    public static  Shelve toShelve(ShelveDto shelveDto, Warehouse warehouse){
        Dimensions dimensions =DimensionsMapper.toDimensions(shelveDto.getDimensions(), warehouse.getUnit());
        Set<ShelveTier> tiers = ShelveTierMapper.toTierSet(shelveDto.getTiers(), warehouse.getUnit());
        return new Shelve(shelveDto.getId(), shelveDto.getNumber(), shelveDto.getName(), dimensions, shelveDto.isSize(), tiers, warehouse, shelveDto.getRow());
    }
    public static ShelveDto toShelveDto(Shelve shelve) {
        DimensionsDto dimensions =DimensionsMapper.toDto(shelve.getDimensions());
       List<ShelveTierDto> tiers = ShelveTierMapper.toTierDtoList(shelve.getShelveTiers());
       Boolean freeSpace=null;
        if(tiers!=null) {
           freeSpace = shelve.getShelveTiers().stream().anyMatch(t -> t.getOccupiedVolume()/ t.getDimensions().getVolume() < 1);
       }
        return new ShelveDto(shelve.getId(), shelve.getNumber(), shelve.getName(),shelve.isSize(), dimensions, tiers, freeSpace, shelve.getRow());
    }

    public static List<ShelveDto> toShelveDtos(List<Shelve> shelves) {
        return shelves.stream().map(ShelveMapper::toShelveDto).sorted(Comparator.comparing(ShelveDto::getNumber)).toList();
    }

    public static List<FreeShelveDto> toFreeShelveDto(List<ShelveTier> tiers) {
        List<FreeShelveDto> shelveDtos = new ArrayList<>();
        for (ShelveTier tier : tiers) {
            boolean tierAdded = addTierToFreeShelve(shelveDtos, tier);
            if (!tierAdded) {
                createFreeShelve(shelveDtos, tier);
            }
        }
        return shelveDtos;
    }

    private static boolean addTierToFreeShelve(List<FreeShelveDto> shelveDtos, ShelveTier tier) {
        for (FreeShelveDto s : shelveDtos) {
            if (s.getShelve().id().equals(tier.getShelve().getId())) {
                s.getTiers().add(ShelveTierMapper.toBasicShelveTierInfoDto(tier));
                return true;
            }
        }
        return  false;
    }

    private static void createFreeShelve(List<FreeShelveDto> shelveDtos, ShelveTier tier){
        BasicShelveInfoDto shelve = new BasicShelveInfoDto(tier.getShelve().getId(), tier.getShelve().getNumber(), tier.getShelve().getName());
        BasicShelveTierInfoDto tierDto = ShelveTierMapper.toBasicShelveTierInfoDto(tier);
        Comparator<BasicShelveTierInfoDto> comparator = Comparator.comparingInt(BasicShelveTierInfoDto::getNumber);
        TreeSet<BasicShelveTierInfoDto> tiers = new TreeSet<>(comparator);
        tiers.add(tierDto);
        FreeShelveDto freeShelveDto = FreeShelveDto.builder()
                .shelve(shelve)
                .tiers(tiers)
                .build();
        shelveDtos.add(freeShelveDto);
    }
}
