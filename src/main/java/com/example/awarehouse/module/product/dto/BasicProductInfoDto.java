package com.example.awarehouse.module.product.dto;

import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class BasicProductInfoDto {
    private UUID id;
    private String title;
    private double amount;
    private PriceDto price;
    private String photo;
    private BasicGroupInfoDto group;
}
