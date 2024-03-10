package com.example.awarehouse.module.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ProductInTierDto {
    private UUID id;
    private String title;
    private double amount;
    private String image;
}
