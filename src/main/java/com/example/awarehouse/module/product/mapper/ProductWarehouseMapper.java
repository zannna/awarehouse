package com.example.awarehouse.module.product.mapper;

import com.example.awarehouse.module.product.ProductWarehouse;
import com.example.awarehouse.module.product.dto.ProductWarehouseDto;
import com.example.awarehouse.module.warehouse.shelve.Shelve;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;

public class ProductWarehouseMapper {

    public static ProductWarehouseDto toDto(ProductWarehouse productWarehouse){
        if(productWarehouse == null) return null;
        ShelveTier tier = productWarehouse.getTier();
        Integer shelveNumber = null;
        Integer tierNumber =null;
        Integer row = null;
        if (tier!=null) {
            tierNumber = tier.getNumber();
            Shelve shelve = tier.getShelve();
            shelveNumber = shelve.getNumber();
            row = shelve.getRow();
        }
        return new ProductWarehouseDto(productWarehouse.getId(), productWarehouse.getWarehouse().getName(), row,  shelveNumber, tierNumber, productWarehouse.getNumberOfProducts());
    }
}
