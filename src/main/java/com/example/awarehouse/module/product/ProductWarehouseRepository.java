package com.example.awarehouse.module.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, UUID> {
    @Query(value = "insert into product_warehouse (product_id, warehouse_id, number_of_products) values (:productId, :warehouseId, :numberOfProducts) RETURNING *", nativeQuery = true)
    ProductWarehouse createProductWarehouseAssociation(UUID productId, UUID warehouseId, double numberOfProducts);

}
