package com.example.awarehouse.module.product.dto;

import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.product.Price;
import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;
import java.util.UUID;

import static com.example.awarehouse.module.product.util.ProductConstants.LINK_HAS_INVALID_FORMAT;
import static com.example.awarehouse.module.product.util.ProductConstants.LINK_REGEX;

public class ProductCreationDto {
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotNull(message = "Amount is mandatory")
    private double amount;
    @Valid
    private PriceDto price;
    @Pattern(regexp = LINK_REGEX, message = LINK_HAS_INVALID_FORMAT)
    private String photo;
    private UUID groupId;
    private UUID warehouseId;
}
