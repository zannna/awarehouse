package com.example.awarehouse.module.warehouse.shelve.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Builder
@AllArgsConstructor
@Getter
public class FreeShelveDto {
    BasicShelveInfoDto shelve;
    TreeSet<BasicShelveTierInfoDto> tiers;
}
