package com.example.awarehouse.module.warehouse.shelve.tier;

import com.example.awarehouse.module.product.ProductWarehouseService;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.ShelveNotExist;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ShelveTierService {
    ShelveTierRepository shelveTierRepository;
    ProductWarehouseService productWarehouseService;
    public ShelveTier getShelveTier( UUID warehouseId, Integer shelveNumber, Integer tierNumber) {
        ShelveTier tier = shelveTierRepository.findByShelveWarehouseIdAndShelveNumberAndNumber(warehouseId, shelveNumber, tierNumber).orElseThrow(() -> new ShelveNotExist("Shelve with number " + shelveNumber + " does not have tier with number " + tierNumber));
        return tier;
    }

    public void saveAllShelves( Set<ShelveTier> tiers){
        shelveTierRepository.saveAll(tiers);
    }

    public List<ShelveTier> findFreePlace(double volume, List<UUID> warehouseIds) {
        return shelveTierRepository.findFreeTiers(volume, warehouseIds);
    }


    public void removeShelfTier(UUID tierId){
        productWarehouseService.moveProductFromTier(tierId);
        int count = shelveTierRepository.deleteTier(tierId);
        if(count==0){
            throw new ShelveNotExist("Shelve with id "+tierId+" does not exist");
        }
    }

    public void removeShelfTiers(Set<ShelveTier> shelveTiers) {
        shelveTiers.forEach(tier -> removeShelfTier(tier.getId()));
    }
}
