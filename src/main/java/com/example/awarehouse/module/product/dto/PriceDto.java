package com.example.awarehouse.module.product.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.checkerframework.checker.units.qual.N;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@AllArgsConstructor
public class PriceDto {
    @DecimalMin(value = "0", message = "Price must be positive")
    private BigDecimal amount;

    @NotNull(message = "Currency is mandatory")
    private Currency currency;
}
