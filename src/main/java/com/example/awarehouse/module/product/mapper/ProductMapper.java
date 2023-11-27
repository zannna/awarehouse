package com.example.awarehouse.module.product.mapper;

import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.group.mapper.WarehouseGroupMapper;
import com.example.awarehouse.module.product.Product;
import com.example.awarehouse.module.product.ProductWarehouse;
import com.example.awarehouse.module.product.dto.ProductDTO;
import com.example.awarehouse.module.warehouse.dto.BasicWarehouseInfoDto;
import com.example.awarehouse.module.warehouse.mapper.WarehouseMapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductDTO mapToDto(Product product) {

        return ProductDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .amount(product.getAmount())
                .price(product.getPrice())
                .photo(product.getPhoto())
                .group(WarehouseGroupMapper.toDto(product.getGroup()))
                .productWarehouses(getWarehousesFromProduct(product))
                .build();
    }
     private static Set<BasicWarehouseInfoDto> getWarehousesFromProduct(Product product) {
        Set<ProductWarehouse> productWarehouses = product.getProductWarehouse();
        if(productWarehouses == null)
            return new HashSet<>();
         return productWarehouses.stream()
                 .map(pw->WarehouseMapper.toBasicWarehouseInfoDto(pw.getWarehouse()))
                 .collect(Collectors.toSet());
     }

}
