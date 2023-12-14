package com.example.awarehouse.module.warehouse.shelve;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.awarehouse.module.product.dto.ProductCreationDto;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WarehouseService;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.shelve.dto.*;
import com.example.awarehouse.module.warehouse.shelve.mapper.ShelveMapper;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTierService;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.ShelveNotExist;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WarehouseNotExistException;
import com.example.awarehouse.util.UserIdSupplier;
import jakarta.persistence.EntityManager;
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
        checkTierDimension(shelveDto.getTiers());
        Warehouse warehouse=warehouseService.getWarehouse(warehouseId).orElseThrow(()->new WarehouseNotExistException("Warehouse with id "+warehouseId+" does not exist"));
        Shelve shelve= ShelveMapper.toShelve(shelveDto, warehouse);
        Shelve savedShelve = shelveRepository.save(shelve);
        setTiers(savedShelve);
        return ShelveMapper.toShelveDto(savedShelve);
    }

    private void validateIfShelveNumberNotExistInWarehouse(UUID warehouseId, int number){
        boolean isShelveNumberExist = shelveRepository.findByWarehouseIdAndNumber(warehouseId, number).isPresent();
        if(isShelveNumberExist){
            throw new IllegalArgumentException("Shelve with number "+number+" already exist");
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

    private void checkTierDimension(List<ShelveTierCreationDto> tierDtos){
        tierDtos.stream()
                .filter(ShelveTierCreationDto::isSize)
                .forEach(tier -> checkDimension(tier.getDimensions()));

    }

    private void setTiers(Shelve savedShelve){
        Set<ShelveTier> tiers =  savedShelve.getShelveTiers();
        tiers.forEach(tier -> tier.setShelve(savedShelve));
        shelveTierService.saveAllShelves(tiers);
    }


}
