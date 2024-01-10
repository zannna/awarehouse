package com.example.awarehouse.module.warehouse.shelve.tier;

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
}
