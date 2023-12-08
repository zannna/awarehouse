package com.example.awarehouse.module.product.mapper;

import com.example.awarehouse.module.group.mapper.WarehouseGroupMapper;
import com.example.awarehouse.module.product.Price;
import com.example.awarehouse.module.product.Product;
import com.example.awarehouse.module.product.ProductWarehouse;
import com.example.awarehouse.module.product.dto.PriceDto;
import com.example.awarehouse.module.product.dto.ProductCreationDto;
import com.example.awarehouse.module.product.dto.ProductDTO;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import com.example.awarehouse.module.warehouse.mapper.WarehouseMapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductDTO withGroupToDto(Product product) {
        return mapToDto(product, null, product.getAmount());
    }
    public static ProductDTO withWarehouseToDto(Product product, ProductWarehouse productWarehouse) {
        return mapToDto(product, WarehouseMapper.toBasicWarehouseInfoDto(productWarehouse.getWarehouse()), productWarehouse.getNumberOfProducts());
    }
     private static Set<BasicWarehouseInfoDto> getWarehousesFromProduct(Product product) {
        Set<ProductWarehouse> productWarehouses = product.getProductWarehouses();
        if(productWarehouses == null)
            return new HashSet<>();
         return productWarehouses.stream()
                 .map(pw->WarehouseMapper.toBasicWarehouseInfoDto(pw.getWarehouse()))
                 .collect(Collectors.toSet());
     }

//    public static ProductDTO mapToDto(Product savedProduct, ProductWarehouse productWarehouse) {
//        return mapToDto(savedProduct, WarehouseMapper.toBasicWarehouseInfoDto(productWarehouse.getWarehouse())));
//    }
   private static ProductDTO mapToDto(Product savedProduct, BasicWarehouseInfoDto  warehouse, double amount){
        PriceDto priceDto = toPriceDto(savedProduct.getPrice());
       return ProductDTO.builder()
                .id(savedProduct.getId())
                .title(savedProduct.getTitle())
                .amount(amount)
                .price(priceDto)
                .photo(savedProduct.getPhoto())
                .group(WarehouseGroupMapper.toDto(savedProduct.getGroup()))
                .productWarehouses(warehouse)
                .build();
    }
    private static PriceDto toPriceDto(Price price){
        return new PriceDto(price.getAmount(), price.getCurrency());
    }
}
