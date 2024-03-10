package com.example.awarehouse.module.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class RowWithProducts {
    private int row;
    private List<ShelfWithProductsDto> shelves;
}
