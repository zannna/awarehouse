package com.example.awarehouse.module.product;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Price {

    @DecimalMin(value = "0", message = "{conference.priceAmount.positive}")
    @Column(name = "price_amount")
    private BigDecimal amount;

    @Column(name = "price_currency")
    private Currency currency;

}
