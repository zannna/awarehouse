package com.example.awarehouse.module.product;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.warehouse.shelve.Dimensions;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String title;

    private double amount;

    @Embedded
    private Price price;


    private String photo;

    @Embedded
    private Dimensions dimensions;

    @ManyToOne
    @JoinColumn(name = "group_id")
   private WarehouseGroup group;

   @OneToMany(mappedBy = "product",  fetch = FetchType.EAGER)
   private Set<ProductWarehouse> productWarehouses = new HashSet<>();

    public Product(String title, double amount, Price price, String photo, WarehouseGroup group) {
        this.title = title;
        this.amount = amount;
        this.price = price;
        this.photo = photo;
        this.group = group;
    }

    public Set<ProductWarehouse> addProductWarehouse(ProductWarehouse productWarehouse){
        productWarehouses.add(productWarehouse);
        return productWarehouses;
    }

}
