package com.example.awarehouse.module.product;

import jakarta.persistence.Embedded;

public class Product {

    String photo;
    String title;
    double amount;

    @Embedded
    Price price;
    String description;
}
