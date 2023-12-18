package com.example.awarehouse.module.warehouse.shelve;

import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WarehouseService;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.shelve.dto.*;
import com.example.awarehouse.module.warehouse.shelve.mapper.ShelveMapper;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTierService;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WarehouseNotExistException;
import com.example.awarehouse.util.UserIdSupplier;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ShelveService {
    private final WarehouseService warehouseService;
    private final ShelveRepository shelveRepository;
    private final WorkerWarehouseService workerWarehouseService;
    private final UserIdSupplier workerIdSupplier;
    private final ShelveTierService shelveTierService;

    public ShelveDto createShelve(UUID warehouseId, ShelveCreationDto shelveDto) {
        workerWarehouseService.validateWorkerWarehouseRelation(  workerIdSupplier.getUserId(), warehouseId);
        validateIfShelveNumberNotExistInWarehouse(warehouseId,shelveDto.getNumber());
        checkShelveDimension(shelveDto);
        boolean sameSizeTiers = shelveDto.isSameSizeTiers();
        checkTierDimension(sameSizeTiers,shelveDto.getTiers(), shelveDto.getDimensions());
        Warehouse warehouse=warehouseService.getWarehouse(warehouseId).orElseThrow(()->new WarehouseNotExistException("Warehouse with id "+warehouseId+" does not exist"));
        Shelve shelve= ShelveMapper.toShelve(shelveDto, warehouse);
        Shelve savedShelve = shelveRepository.save(shelve);
        setTiers(savedShelve, sameSizeTiers);
        return ShelveMapper.toShelveDto(savedShelve);
    }

    private void validateIfShelveNumberNotExistInWarehouse(UUID warehouseId, int number){
        boolean isShelveNumberExist = shelveRepository.findByWarehouseIdAndNumber(warehouseId, number).isPresent();
        if(isShelveNumberExist){
            throw new IllegalArgumentException("Shelve with number "+number+" already exist");
        }
    }

    private void checkTierDimension(DimensionsDto dimensions, DimensionsDto shelveDimension){
        checkDimension(dimensions);
        if(dimensions.height()>shelveDimension.height() || dimensions.length()>shelveDimension.length() || dimensions.width()>shelveDimension.width()){
            throw new IllegalArgumentException("Tier dimensions can not be bigger than shelve dimensions");
        }
    }
    private void checkDimension(DimensionsDto dimensions){
        if(dimensions.height()==0 || dimensions.length()==0 || dimensions.width()==0){
            throw new IllegalArgumentException("When size is checked, dimensions must have a height, length and width");
        }
    }

    private void checkShelveDimension(ShelveCreationDto shelveDto){
        if(shelveDto.isSize()){
            checkDimension(shelveDto.getDimensions());
        }
    }

    private void checkTierDimension(boolean setSameSizeTiers,List<ShelveTierCreationDto> tierDtos, DimensionsDto shelveDimension){
        if(!setSameSizeTiers){
            tierDtos.stream()
                    .filter(ShelveTierCreationDto::isSize)
                    .forEach(tier -> checkTierDimension(tier.getDimensions(), shelveDimension));
        }
    }

    private void setTiers(Shelve savedShelve, boolean sameSizeTiers){
        Set<ShelveTier> tiers =  savedShelve.getShelveTiers();
        tiers.forEach(tier -> tier.setShelve(savedShelve));
        if (sameSizeTiers){
            setSameSizeDimensionInTiers(savedShelve, tiers);
        }
        shelveTierService.saveAllShelves(tiers);
    }

    private void setSameSizeDimensionInTiers(Shelve savedShelve,  Set<ShelveTier> tiers ) {
        Dimensions dimensions = savedShelve.getDimensions();
        int tierNumber = tiers.size();
        Dimensions tierDimension = new Dimensions(Math.round(dimensions.getHeight()/tierNumber), dimensions.getLength(), dimensions.getWidth());
        tiers.forEach(tier -> tier.setDimensions(tierDimension));
    }

    public List<ShelveDto> getShelves(UUID warehouseId) {
        workerWarehouseService.validateWorkerWarehouseRelation(workerIdSupplier.getUserId(), warehouseId);
        List<Shelve> shelves = shelveRepository.findAllByWarehouseIdOrderByNumber(warehouseId);
        return ShelveMapper.toShelveDtos(shelves);
    }

}
