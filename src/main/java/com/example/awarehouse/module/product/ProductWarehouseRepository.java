package com.example.awarehouse.module.product;

import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Repository
interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, UUID> {
    @Query(value = "insert into product_warehouse (product_id, warehouse_id, number_of_products) values (:productId, :warehouseId, :numberOfProducts) RETURNING *", nativeQuery = true)
    ProductWarehouse createProductWarehouseAssociation( @Param("productId") UUID productId, @Param("warehouseId") UUID warehouseId,  @Param("numberOfProducts") double numberOfProducts);

    @Query(value="select pw from ProductWarehouse pw where pw.warehouse.id =:warehouseId")
    Page<ProductWarehouse> getAllProductsByWarehouseId( Pageable pageable, @Param("warehouseId")  UUID warehouseId);

    @Query(value="select pw from ProductWarehouse pw where pw.warehouse.id in :warehouseIds")
    List<ProductWarehouse> getAllProductsFromWarehouses(Pageable pageable,@Param("warehouseIds") List<UUID> warehouseIds);

    @Query(value="select pw from ProductWarehouse ")
}
