package com.example.awarehouse.module.product.mapper;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.mapper.WarehouseGroupMapper;
import com.example.awarehouse.module.product.Price;
import com.example.awarehouse.module.product.Product;
import com.example.awarehouse.module.product.ProductWarehouse;
import com.example.awarehouse.module.product.dto.*;
import com.example.awarehouse.module.warehouse.shelve.mapper.DimensionsMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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


    public static UnderstockedProductInWarehouseDto toUnderstockedProductInWarehouseDto(Product product) {
        String groupName = product.getGroup() == null ? "" : product.getGroup().getName();
        return new UnderstockedProductInWarehouseDto(product.getId(), product.getTitle(), toPriceString(product.getPrice()), groupName);
    }

    private static String toPriceString(Price price) {
        return  price.getAmount() + " " + price.getCurrency();
    }

    public static UnderstockedProductInGroupDto toUnderstockedProductInGroupDto(Product product) {
        String warehouses = toWarehouseNameStringList( product.getProductWarehouses());
        return new UnderstockedProductInGroupDto(product.getId(), product.getTitle(),toPriceString(product.getPrice()), warehouses);
    }
    private static String toWarehouseNameStringList(Set<ProductWarehouse> productWarehouses){
        String warehouses = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (ProductWarehouse productWarehouse : productWarehouses) {
            stringBuilder.append(productWarehouse.getWarehouse().getName()).append(",");
        }
        if (!stringBuilder.isEmpty()) {
            warehouses = stringBuilder.substring(0, stringBuilder.length() - 1);
        }
        return warehouses;
    }

    public static Product toProduct(ProductCreationDto productDto, WarehouseGroup group) {
        return Product.builder()
                .title(productDto.getTitle())
                .amount(productDto.getAmountGroup())
                .price(new Price(productDto.getPrice().getAmount(), productDto.getPrice().getCurrency()))
                .photo(productDto.getPhoto())
                .dimensions(DimensionsMapper.toDimensions(productDto.getDimensions()))
                .group(group)
                .build();
    }
}
