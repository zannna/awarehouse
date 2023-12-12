package com.example.awarehouse.module.product.mapper;

import com.example.awarehouse.module.group.mapper.WarehouseGroupMapper;
import com.example.awarehouse.module.product.Price;
import com.example.awarehouse.module.product.Product;
import com.example.awarehouse.module.product.dto.PriceDto;
import com.example.awarehouse.module.product.dto.ProductDto;
import com.example.awarehouse.module.product.dto.ProductWarehouseDto;
import com.example.awarehouse.module.warehouse.Warehouse;

import java.util.List;

public class ProductMapper {

    public static ProductDto toDto(Product product,  List<ProductWarehouseDto> productWarehouses){
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .amount(product.getAmount())
                .price(toPriceDto(product.getPrice()))
                .photo(product.getPhoto())
                .group(WarehouseGroupMapper.toDto(product.getGroup()))
                .productWarehouses(productWarehouses)
                .build();
    }
    private static PriceDto toPriceDto(Price price){
        return new PriceDto(price.getAmount(), price.getCurrency());
    }
}
