package com.example.awarehouse.module.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface ProductRepository extends JpaRepository< Product, UUID> {
//    @Modifying
//    @Query(nativeQuery = true, value = "INSERT INTO product (:title, :amount, :price_amount, :price_currency)\n" +
//            "SELECT  title, amount, price_amount, price_currency\n" +
//            "FROM product\n" +
//            "WHERE title=:title, amount=:amount, price_amount=:price_amount, price_currency=:price_currency\n" +
//            "ON CONFLICT (column1) DO NOTHING;")
//    void insertUniquely(@Param("type") final String type, @Param("src") final UUID source);

}
