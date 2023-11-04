package com.example.awarehouse.module.warehouse.dto;

import com.example.awarehouse.module.warehouse.LengthUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;

import java.util.Set;

public record WarehouseCreation(@NotBlank String name, @NonNull LengthUnit unit, @Min(value = 0) int numberOfRows,
                                Set<Long> groupIds) {
}
