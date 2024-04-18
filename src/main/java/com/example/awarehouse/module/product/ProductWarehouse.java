package com.example.awarehouse.module.product;

import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "product_warehouse")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ProductWarehouse {
    @Id
    @Column(name = "pw_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name= "warehouse_id")
    private Warehouse warehouse;

   private double numberOfProducts;

    @ManyToOne
    @JoinColumn(name= "tier_id")
    private ShelveTier tier;


    public ProductWarehouse(Product product, Warehouse warehouse, double numberOfProducts){
        this.product = product;
        this.warehouse = warehouse;
        this.numberOfProducts = numberOfProducts;
    }

    public ProductWarehouse(Product product, Warehouse warehouse, double numberOfProducts, ShelveTier tier) {
       this(product, warehouse,numberOfProducts);
        this.tier =tier;
    }

    public void setNumberOfProducts(double numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }


}
