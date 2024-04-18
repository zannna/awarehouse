package com.example.awarehouse.module.product.util;

import com.example.awarehouse.module.product.dto.MoveProductsDto;

import java.util.Set;
import java.util.UUID;

import static com.example.awarehouse.module.warehouse.util.WarehouseTestConstants.WAREHOUSE_ID;

public class ProductFactory {
   public static  MoveProductsDto createMoveProductsDto(){
        MoveProductsDto.ProductWarehouseMoveInfo productWarehouseMoveInfo = new MoveProductsDto.ProductWarehouseMoveInfo(UUID.fromString("85d38561-3084-4723-8c2a-0ec0c22df642"), 3.0);
        MoveProductsDto.ProductWarehouseMoveInfo productWarehouseMoveInfo2 = new MoveProductsDto.ProductWarehouseMoveInfo(UUID.fromString("35b4cabd-1f9c-4c97-8b09-03a5a7450505"), 8.0);
        return new MoveProductsDto(Set.of(productWarehouseMoveInfo, productWarehouseMoveInfo2 ), WAREHOUSE_ID, 1, 2);
    }

    public static String createMoveProductsDtoJson(){
       return """
               {
                   "warehouseId":"1b0cbc82-236f-4846-ac08-6d88baa91294",
                    "shelfNumber":1,
                    "tierNumber":1,
                    "productWarehouseMoveInfos":[
                        {
                            "productWarehouseId":"c6e1b3aa-948d-11ee-b9d1-0242ac120002",
                            "amount":5.0
                        }
                    ]
                }
               """;
    }

    public static String createMoveProductsDtoJsonWithNullWarehouseId(){
        return """
               {
                    "shelfNumber":1,
                    "tierNumber":1,
                    "productWarehouseMoveInfos":[
                        {
                            "productWarehouseId":"c6e1b3aa-948d-11ee-b9d1-0242ac120002",
                            "amount":5.0
                        }
                    ]
                }
               """;
    }

    public static String createMoveProductsDtoJsonWithNullShelf(){
        return """
               {
                   "warehouseId":"1b0cbc82-236f-4846-ac08-6d88baa91294",
                    "tierNumber":1,
                    "productWarehouseMoveInfos":[
                        {
                            "productWarehouseId":"c6e1b3aa-948d-11ee-b9d1-0242ac120002",
                            "amount":5.0
                        }
                    ]
                }
               """;
    }

    public static String createMoveProductsDtoJsonWithNullTier(){
        return """
               {
                   "warehouseId":"1b0cbc82-236f-4846-ac08-6d88baa91294",
                    "shelfNumber":1,
                    "productWarehouseMoveInfos":[
                        {
                            "productWarehouseId":"c6e1b3aa-948d-11ee-b9d1-0242ac120002",
                            "amount":5.0
                        }
                    ]
                }
               """;
    }
}
