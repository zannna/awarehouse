package com.example.awarehouse.module.product;

import com.example.awarehouse.module.warehouse.WorkerWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, UUID> {
    @Query(value = "insert into product_warehouse (product_id, warehouse_id, number_of_products) values (:productId, :warehouseId, :numberOfProducts) RETURNING *", nativeQuery = true)
    ProductWarehouse createProductWarehouseAssociation(UUID productId, UUID warehouseId, double numberOfProducts);

}
