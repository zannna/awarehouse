package com.example.awarehouse.module.product.dto;

import com.example.awarehouse.module.warehouse.shelve.dto.DimensionsDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

import static com.example.awarehouse.module.product.util.ProductConstants.LINK_HAS_INVALID_FORMAT;
import static com.example.awarehouse.module.product.util.ProductConstants.LINK_REGEX;

@AllArgsConstructor
@Getter
public class ProductCreationDto {
    @NotBlank(message = "Title is mandatory")
    private String title;
    private Double amountGroup;
    @Valid
    private PriceDto price;
    @Pattern(regexp = LINK_REGEX, message = LINK_HAS_INVALID_FORMAT)
    private String photo;
    private UUID groupId;
    private UUID warehouseId;
    private DimensionsDto dimensions;
    private List<ProductWarehouseCreationDto> productWarehouses;
}
