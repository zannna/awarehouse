package com.example.awarehouse.module.product.mapper;

import com.example.awarehouse.module.group.mapper.WarehouseGroupMapper;
import com.example.awarehouse.module.product.Price;
import com.example.awarehouse.module.product.Product;
import com.example.awarehouse.module.product.ProductWarehouse;
import com.example.awarehouse.module.product.dto.BasicProductInfoDto;
import com.example.awarehouse.module.product.dto.PriceDto;
import com.example.awarehouse.module.product.dto.ProductDto;
import com.example.awarehouse.module.product.dto.ProductWarehouseDto;
import com.example.awarehouse.module.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public static BasicProductInfoDto toBasicDto(Product product) {
        return new BasicProductInfoDto(product.getId(), product.getTitle(), product.getAmount(), toPriceDto(product.getPrice()), product.getPhoto(), WarehouseGroupMapper.toDto(product.getGroup()));
    }
    public static ProductDto toDto(ProductWarehouse productWarehouse) {
        List<ProductWarehouseDto> productWarehouses = new ArrayList<>();
        productWarehouses.add(ProductWarehouseMapper.toDto(productWarehouse));
        return toDto(productWarehouse.getProduct(), productWarehouses);
    }
    public static ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .amount(product.getAmount())
                .price(toPriceDto(product.getPrice()))
                .photo(product.getPhoto())
                .group(WarehouseGroupMapper.toDto(product.getGroup()))
                .build();
    }
        public static ProductDto toDto(Product product,  List<ProductWarehouseDto> productWarehouses) {
            ProductDto productDto = toDto(product);
            productDto.setProductWarehouses(productWarehouses);
            return productDto;
        }

    private static PriceDto toPriceDto(Price price){
        return new PriceDto(price.getAmount(), price.getCurrency());
    }

}
