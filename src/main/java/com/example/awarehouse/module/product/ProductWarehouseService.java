package com.example.awarehouse.module.product;

import com.example.awarehouse.module.product.dto.MoveProductsDto;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductWarehouseService {
    ProductWarehouseRepository productWarehouseRepository;
    public void moveProductsToTier(Set<MoveProductsDto.ProductWarehouseMoveInfo> productWarehouseMoveList, ShelveTier tier){
        Map<UUID, Double> productWarehouseMoveInfos= productWarehouseMoveList
                .stream()
                .collect(Collectors.toMap(
                        MoveProductsDto.ProductWarehouseMoveInfo::getProductWarehouseId,
                        MoveProductsDto.ProductWarehouseMoveInfo::getAmount,
                        (existing, replacement) -> existing));
        List<ProductWarehouse> productWarehouses = productWarehouseRepository.findAllById(productWarehouseMoveInfos.keySet());
        for (ProductWarehouse productWarehouse : productWarehouses) {
            Double amount =productWarehouseMoveInfos.get(productWarehouse.getId());
            removeProductFromTier(amount, productWarehouse);
            ProductWarehouse newProductWarehouse = new ProductWarehouse(productWarehouse.getProduct(), tier.getShelve().getWarehouse(), amount, tier);
            productWarehouseRepository.save(newProductWarehouse);
        }
    }

    private void removeProductFromTier( Double amount, ProductWarehouse productWarehouse){
        removeOccupiedVolumeFromTier(amount, productWarehouse);
        if(amount <= productWarehouse.getNumberOfProducts()){
            productWarehouseRepository.delete(productWarehouse);
        }
        else{
            productWarehouse.setNumberOfProducts(productWarehouse.getNumberOfProducts()-amount);
            productWarehouseRepository.save(productWarehouse);
        }
    }
    public void removeOccupiedVolumeFromTiers( List<ProductWarehouse>  productWarehouses ){
        for (ProductWarehouse productWarehouse : productWarehouses) {
            removeOccupiedVolumeFromTier(productWarehouse.getNumberOfProducts(), productWarehouse);
        }
    }
    private void removeOccupiedVolumeFromTier(Double amount, ProductWarehouse productWarehouse){
        Double volume = productWarehouse.getProduct().getDimensions().getVolume();
        productWarehouse.getTier().removeOccupiedVolume(volume*amount);
    }
    public void moveProductFromTier(UUID tierId){
        productWarehouseRepository.setTier(null, tierId);
    }

    public void removeProductWarehousesByProductIds(List<UUID> productIds){
        List<ProductWarehouse>  productWarehouses = productWarehouseRepository.findAllByProductIdIn(productIds);
        removeOccupiedVolumeFromTiers(productWarehouses);
        productWarehouseRepository.deleteAll(productWarehouses);
    }
    public void removeProductWarehouses(List<UUID> productWarehouseIds){
        List<ProductWarehouse>  productWarehouses = productWarehouseRepository.findAllById(productWarehouseIds);
        for (ProductWarehouse productWarehouse : productWarehouses) {
            removeOccupiedVolumeFromTier(productWarehouse.getNumberOfProducts(), productWarehouse);
            productWarehouse.getProduct().subtractAmount(productWarehouse.getNumberOfProducts());
        }
        productWarehouseRepository.deleteAll(productWarehouses);
    }

}
