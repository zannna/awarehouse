package com.example.awarehouse.module.product.util;

import com.example.awarehouse.module.product.dto.MoveProductsDto;

import java.util.Set;
import java.util.UUID;

import static com.example.awarehouse.module.warehouse.util.WarehouseTestConstants.WAREHOUSE_ID;

public class ProductFactory {
   public static  MoveProductsDto createMoveProductsDto(){
        MoveProductsDto.ProductWarehouseMoveInfo productWarehouseMoveInfo = new MoveProductsDto.ProductWarehouseMoveInfo(UUID.fromString("c6e1b3aa-948d-11ee-b9d1-0242ac120002"), 5.0);
        return new MoveProductsDto(Set.of(productWarehouseMoveInfo), WAREHOUSE_ID, 1, 1);
    }

    public static String createMoveProductsDtoJson(){
       return """
               
               """;
    }
}
