package com.example.awarehouse.module.warehouse.dto;

import com.example.awarehouse.module.warehouse.util.unit.LengthUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

import java.util.Set;
import java.util.UUID;

public record WarehouseCreation(   @NotBlank(message = "Name cannot be blank") String name,
                                   @NotNull(message = "Unit cannot be null") LengthUnit unit,
                                   @Min(value = 0, message = "Number of rows must be greater than or equal to 0") int numberOfRows,
                                   Set<UUID> groupIds) {
}
