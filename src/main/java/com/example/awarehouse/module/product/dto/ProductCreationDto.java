package com.example.awarehouse.module.product.dto;

import com.example.awarehouse.module.warehouse.shelve.dto.DimensionsDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductCreationDto {
    @NotBlank(message = "Title is mandatory")
    private String title;
    private Double amountGroup;
    @Valid
    private PriceDto price;
    private String image;
    private UUID groupId;
    // do usunięcia
    private UUID warehouseId;
    private DimensionsDto dimensions;
    private List<ProductWarehouseCreationDto> productWarehouses;
}
