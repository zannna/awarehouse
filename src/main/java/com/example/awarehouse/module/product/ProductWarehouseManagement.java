package com.example.awarehouse.module.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductWarehouseManagement {
    ProductWarehouseRepository productWarehouseRepository;
    public void moveProductFromTier(UUID tierId){
        productWarehouseRepository.setTier(null, tierId);
    }
}
