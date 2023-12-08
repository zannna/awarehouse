package com.example.awarehouse.module.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

import static com.example.awarehouse.module.product.util.ProductConstants.LINK_HAS_INVALID_FORMAT;
import static com.example.awarehouse.module.product.util.ProductConstants.LINK_REGEX;

@AllArgsConstructor
@Getter
public class ProductCreationDto {
    @NotBlank(message = "Title is mandatory")
    private String title;
    private double amountGroup;
    private double amountWarehouse;
    @Valid
    private PriceDto price;
    @Pattern(regexp = LINK_REGEX, message = LINK_HAS_INVALID_FORMAT)
    private String photo;
    private UUID groupId;
    private UUID warehouseId;
}
