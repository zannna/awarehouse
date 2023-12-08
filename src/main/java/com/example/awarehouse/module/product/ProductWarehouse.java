package com.example.awarehouse.module.product;

import com.example.awarehouse.module.warehouse.Role;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.worker.Worker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_warehouse")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ProductWarehouse {
    @Id
    @Column(name = "pw_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name= "warehouse_id")
    private Warehouse warehouse;

   private double numberOfProducts;

    public ProductWarehouse(Product product, Warehouse warehouse, double numberOfProducts) {
        this.product = product;
        this.warehouse = warehouse;
        this.numberOfProducts = numberOfProducts;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }
}
