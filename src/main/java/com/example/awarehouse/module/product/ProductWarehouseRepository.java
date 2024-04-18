package com.example.awarehouse.module.product;

import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, UUID>, JpaSpecificationExecutor<ProductWarehouse> {
    @Query(value = "insert into product_warehouse (product_id, warehouse_id, number_of_products) values (:productId, :warehouseId, :numberOfProducts) RETURNING *", nativeQuery = true)
    ProductWarehouse createProductWarehouseAssociation( @Param("productId") UUID productId, @Param("warehouseId") UUID warehouseId,  @Param("numberOfProducts") double numberOfProducts);

    @Query(value="select pw from ProductWarehouse pw where pw.warehouse.id =:warehouseId")
    Page<ProductWarehouse> getAllProductsByWarehouseId( Pageable pageable, @Param("warehouseId")  UUID warehouseId);

    @Query(value="select pw from ProductWarehouse pw where pw.warehouse.id in :warehouseIds order by pw.product.title asc")
    Page<ProductWarehouse> getAllProductsFromWarehouses(Pageable pageable,@Param("warehouseIds") List<UUID> warehouseIds);

    @Query("SELECT pw.product FROM ProductWarehouse pw JOIN pw.product p WHERE pw.warehouse.id = :warehouseId GROUP BY p HAVING SUM(pw.numberOfProducts) <= 0")
    List<Product> findUnderstockByWarehouse(UUID warehouseId);

    @Modifying
    @Transactional
    @Query("UPDATE ProductWarehouse pw SET pw.tier = :tier WHERE pw.tier.id = :tierId")
    void setTier(ShelveTier tier, UUID tierId);

    List<ProductWarehouse> findAllByProductIdIn(List<UUID> productIds);

    List<ProductWarehouse> findAllByTierIn(List<ShelveTier> tiers);

    List<ProductWarehouse> findAllByTierIn(Set<ShelveTier> tiers);

    List<ProductWarehouse> findAllByTierId(UUID tierId);
}
