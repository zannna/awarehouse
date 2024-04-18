package com.example.awarehouse.module.product;

import com.example.awarehouse.module.product.dto.MoveProductsDto;
import com.example.awarehouse.module.warehouse.shelve.Dimensions;
import com.example.awarehouse.module.warehouse.shelve.Shelve;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductWarehouseServiceTest {

    ProductWarehouseRepository productWarehouseRepository = mock(ProductWarehouseRepository.class);

    @Test
    void moveProductsToTier_whenAllProductsAreToMove_thenShouldDeleteProductWarehouse(){
        //given
        MoveProductsDto.ProductWarehouseMoveInfo moveInfo = new MoveProductsDto.ProductWarehouseMoveInfo(UUID.fromString("4a29fd03-acb6-40ea-8781-10caccc15e71"), 20.0);
        Set<MoveProductsDto.ProductWarehouseMoveInfo> moveInfos = Set.of(moveInfo);
        Product product = Product.builder()
                .dimensions(new Dimensions(0.2,0.2,0.2))
                .build();
        ShelveTier tier = ShelveTier.builder()
                .occupiedVolume(0.8)
                .dimensions(new Dimensions(1,1,1))
                .build();
        ProductWarehouse productWarehouse = ProductWarehouse.builder()
                .id(UUID.fromString("4a29fd03-acb6-40ea-8781-10caccc15e71"))
                .product(product)
                .tier(tier)
                .numberOfProducts(20)
                .build();
        when(productWarehouseRepository.findAllById(any(Iterable.class))).thenReturn(List.of(productWarehouse));

        ShelveTier newTier = ShelveTier.builder()
                .occupiedVolume(0.2)
                .dimensions(new Dimensions(1,1,1))
                .shelve(Shelve.builder().build())
                .build();
        ProductWarehouseService productWarehouseService = new ProductWarehouseService(productWarehouseRepository);

        //when
        productWarehouseService.moveProductsToTier(moveInfos, newTier);

        //then
        assertEquals(0.64, tier.getOccupiedVolume());
        verify(productWarehouseRepository).delete(any(ProductWarehouse.class));
        assertEquals(0.36,  new BigDecimal(newTier.getOccupiedVolume()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        verify(productWarehouseRepository).save(any(ProductWarehouse.class));
    }

    @Test
    void moveProductsToTier_whenSomeProductAreToMove_thenShouldDeleteProductWarehouse(){
        //given
        MoveProductsDto.ProductWarehouseMoveInfo moveInfo = new MoveProductsDto.ProductWarehouseMoveInfo(UUID.fromString("4a29fd03-acb6-40ea-8781-10caccc15e71"), 5.0);
        Set<MoveProductsDto.ProductWarehouseMoveInfo> moveInfos = Set.of(moveInfo);
        Product product = Product.builder()
                .dimensions(new Dimensions(0.2,0.2,0.2))
                .build();
        ShelveTier tier = ShelveTier.builder()
                .occupiedVolume(0.8)
                .dimensions(new Dimensions(1,1,1))
                .build();
        ProductWarehouse productWarehouse = ProductWarehouse.builder()
                .id(UUID.fromString("4a29fd03-acb6-40ea-8781-10caccc15e71"))
                .product(product)
                .tier(tier)
                .numberOfProducts(20)
                .build();
        when(productWarehouseRepository.findAllById(any(Iterable.class))).thenReturn(List.of(productWarehouse));

        ShelveTier newTier = ShelveTier.builder()
                .occupiedVolume(0.2)
                .dimensions(new Dimensions(1,1,1))
                .shelve(Shelve.builder().build())
                .build();
        ProductWarehouseService productWarehouseService = new ProductWarehouseService(productWarehouseRepository);

        //when
        productWarehouseService.moveProductsToTier(moveInfos, newTier);

        //then
        assertEquals(0.76, tier.getOccupiedVolume());
        verify(productWarehouseRepository,  never()).delete(any(ProductWarehouse.class));
        assertEquals(0.24,  new BigDecimal(newTier.getOccupiedVolume()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        verify(productWarehouseRepository, times(2)).save(any(ProductWarehouse.class));
    }

}
