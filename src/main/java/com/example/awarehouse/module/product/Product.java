package com.example.awarehouse.module.product;

import com.example.awarehouse.module.group.WarehouseGroup;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "group_id")
   private WarehouseGroup group;

   @OneToMany(mappedBy = "product")
   private Set<ProductWarehouse> productWarehouse;

}
