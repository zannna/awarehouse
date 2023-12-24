package com.example.awarehouse.module.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
interface ProductRepository extends JpaRepository< Product, UUID> {
    List<Product> findByGroupId(UUID uuid);

    @Query("SELECT p FROM Product p WHERE p.group.id= :groupId AND p.amount=0")
    List<Product> findUnderstockByGroup(UUID groupId);

}
