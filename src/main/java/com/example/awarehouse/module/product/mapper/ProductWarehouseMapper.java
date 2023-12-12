package com.example.awarehouse.module.product.mapper;

import com.example.awarehouse.module.product.ProductWarehouse;
import com.example.awarehouse.module.product.dto.ProductWarehouseCreationDto;
import com.example.awarehouse.module.product.dto.ProductWarehouseDto;
import com.example.awarehouse.module.warehouse.shelve.Shelve;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;

public class ProductWarehouseMapper {

    public static ProductWarehouseDto toDto(ProductWarehouse productWarehouse){
        ShelveTier tier = productWarehouse.getTier();
        int shelveNumber = 0;
        int tierNumber = 0;
        if (tier!=null) {
            tierNumber = tier.getNumber();
            Shelve shelve = tier.getShelve();
            shelveNumber = shelve.getNumber();
        }
        return new ProductWarehouseDto(productWarehouse.getId(), productWarehouse.getWarehouse().getName(),  shelveNumber, tierNumber, productWarehouse.getNumberOfProducts());
    }
}
