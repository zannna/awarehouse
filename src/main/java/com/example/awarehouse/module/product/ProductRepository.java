package com.example.awarehouse.module.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    List<Product> findByGroupId(UUID uuid);

    @Query("SELECT p FROM Product p WHERE p.group.id= :groupId AND p.amount=0")
    List<Product> findUnderstockByGroup(UUID groupId);
    @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.id IN :productIds")
    void deleteProductsById(List<UUID> productIds);

    @Query("SELECT p FROM Product p WHERE p.group.id IN :groupIds")
    List<Product> findProductsWithOnlyGroup(List<UUID> groupIds);
}
