package com.example.awarehouse.module.product.mapper;

import com.example.awarehouse.module.group.WarehouseGroup;
import com.example.awarehouse.module.group.mapper.WarehouseGroupMapper;
import com.example.awarehouse.module.product.Price;
import com.example.awarehouse.module.product.Product;
import com.example.awarehouse.module.product.ProductWarehouse;
import com.example.awarehouse.module.product.dto.*;
import com.example.awarehouse.module.product.util.ImageUtils;
import com.example.awarehouse.module.storage.FileSystemStorageService;
import com.example.awarehouse.module.storage.StorageService;
import com.example.awarehouse.module.warehouse.shelve.Shelve;
import com.example.awarehouse.module.warehouse.shelve.mapper.DimensionsMapper;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ProductMapper {
    private final StorageService storageService = new FileSystemStorageService();

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
                .amountGroup(product.getAmount())
                .price(toPriceDto(product.getPrice()))
                .image(toStringPhoto(product.getPhotoFullName()))
                .group(WarehouseGroupMapper.toDto(product.getGroup()))
                .dimensions(DimensionsMapper.toDto(product.getDimensions()))
                .build();
    }

    private static String toStringPhoto(String fileName) {
        Path imagePath = Paths.get(FileSystemStorageService.rootLocation + "\\" + fileName);
        if (Files.exists(imagePath)) {
            try {
                return ImageUtils.encodeFileToBase64Binary(imagePath);
            } catch (IOException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static ProductDto toDto(Product product, List<ProductWarehouseDto> productWarehouses) {
        ProductDto productDto = toDto(product);
        productDto.setProductWarehouses(productWarehouses);
        return productDto;
    }

    private static PriceDto toPriceDto(Price price) {
        return new PriceDto(price.getAmount(), price.getCurrency());
    }


    public static UnderstockedProductInWarehouseDto toUnderstockedProductInWarehouseDto(Product product) {
        String groupName = product.getGroup() == null ? "" : product.getGroup().getName();
        return new UnderstockedProductInWarehouseDto(product.getId(), product.getTitle(), toPriceString(product.getPrice()), groupName);
    }

    private static String toPriceString(Price price) {
        return price.getAmount() + " " + price.getCurrency();
    }

    public static UnderstockedProductInGroupDto toUnderstockedProductInGroupDto(Product product) {
        String warehouses = toWarehouseNameStringList(product.getProductWarehouses());
        return new UnderstockedProductInGroupDto(product.getId(), product.getTitle(), toPriceString(product.getPrice()), warehouses);
    }

    private static String toWarehouseNameStringList(Set<ProductWarehouse> productWarehouses) {
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

    public static Price toPrice(PriceDto price) {
        return new Price(price.getAmount(), price.getCurrency());
    }

    public static ProductDto toProductDtoWithOnlyGroup(ProductDto product) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .amountGroup(product.getAmountGroup())
                .price(product.getPrice())
                .image(product.getImage())
                .group(product.getGroup())
                .build();
    }
    public static List<RowWithProducts> toShelfWithProductsDtoList(List<ProductWarehouse> productWarehouses,
                                                                   List<ShelveTier> tiers){
        Map<ShelveTier, List<Product>> groupedByTier = productWarehouses.stream()
                .collect(Collectors.groupingBy(
                        ProductWarehouse::getTier,
                        Collectors.mapping(ProductWarehouse::getProduct, Collectors.toList())
                ));
        List<Shelve> shelves = tiers.stream().map(ShelveTier::getShelve).distinct().toList();
        return toShelfWithProductsDtoList(groupedByTier, shelves);
    }


    public static List<RowWithProducts> toShelfWithProductsDtoList(Map<ShelveTier, List<Product>> groupedByTier,
                                                                   List<Shelve> shelves) {
        Map<Integer, List<ShelfWithProductsDto>> shelvesByRow = new HashMap<>();
        for (Shelve shelve : shelves) {
            List<TierWithProductsDto> tiers = toTierWithProductsDtoList(groupedByTier, shelve);
            ShelfWithProductsDto shelfWithProductsDto = ShelfWithProductsDto.builder()
                    .id(shelve.getId())
                    .number(shelve.getNumber())
                    .name(shelve.getName())
                    .hasFreeSpace(shelve.hasFreeSpace())
                    .tiers(tiers)
                    .build();

            shelvesByRow.computeIfAbsent(shelve.getRow(), k -> new ArrayList<>()).add(shelfWithProductsDto);
        }

        return shelvesByRow.entrySet().stream()
                .map(entry -> new RowWithProducts(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


    private static List<TierWithProductsDto> toTierWithProductsDtoList(Map<ShelveTier, List<Product>> groupedByTier, Shelve shelve){
        List<TierWithProductsDto> tierWithProductsDtos = new ArrayList<>();
        for (ShelveTier tier : shelve.getShelveTiers()) {
            List<Product> products = groupedByTier.get(tier);
            TierWithProductsDto tierWithProductsDto = TierWithProductsDto.builder()
                    .id(tier.getId())
                    .number(tier.getNumber())
                    .name(tier.getName())
                    .occupiedVolume(tier.countOccupiedVolumePercentage())
                    .products(ProductMapper.toProductInTierDto(products))
                    .build();
            tierWithProductsDtos.add(tierWithProductsDto);
        }
        return tierWithProductsDtos.stream().sorted(Comparator.comparingInt(TierWithProductsDto::getNumber)).collect(Collectors.toList());
    }

    private static List<ProductInTierDto> toProductInTierDto(List<Product> products) {
        if(products == null || products.isEmpty()){
            return new ArrayList<>();
        }
        return products.stream()
                .map(product -> new ProductInTierDto(product.getId(), product.getTitle(), product.getAmount(), toStringPhoto(product.getPhotoFullName())))
                .collect(Collectors.toList());
    }

}