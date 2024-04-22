package com.example.awarehouse.module.warehouse.shelve;

import com.example.awarehouse.module.product.ProductWarehouseService;
import com.example.awarehouse.module.product.dto.ProductFreePlaceDto;
import com.example.awarehouse.module.product.dto.RowWithProducts;
import com.example.awarehouse.module.product.dto.ShelfWithProductsDto;
import com.example.awarehouse.module.warehouse.Warehouse;
import com.example.awarehouse.module.warehouse.WarehouseService;
import com.example.awarehouse.module.warehouse.WorkerWarehouseService;
import com.example.awarehouse.module.warehouse.shelve.dto.*;
import com.example.awarehouse.module.warehouse.shelve.mapper.ShelveMapper;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTier;
import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTierService;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.ShelfNumberExistInWarehouse;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.WarehouseNotExistException;
import com.example.awarehouse.util.UserIdSupplier;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShelveService {
    private final WarehouseService warehouseService;
    private final ShelveRepository shelveRepository;
    private final WorkerWarehouseService workerWarehouseService;
    private final UserIdSupplier workerIdSupplier;
    private final ShelveTierService shelveTierService;
    private final ProductWarehouseService productWarehouseService;

    @Transactional
    public ShelveDto createShelve(UUID warehouseId, ShelfCreationDto shelveDto) {
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
            throw new ShelfNumberExistInWarehouse("Shelve with number "+number+" already exist");
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

    private void checkShelveDimension(ShelfCreationDto shelveDto){
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
        Dimensions tierDimension = new Dimensions(new BigDecimal(dimensions.getHeight()/tierNumber).setScale(2, RoundingMode.HALF_UP).doubleValue(), dimensions.getLength(), dimensions.getWidth());
        tiers.forEach(tier -> tier.setDimensions(tierDimension));
    }

    public List<ShelveDto> getShelves(UUID warehouseId) {
        workerWarehouseService.validateWorkerWarehouseRelation(workerIdSupplier.getUserId(), warehouseId);
        List<Shelve> shelves = shelveRepository.findAllByWarehouseIdOrderByNumber(warehouseId);
        return ShelveMapper.toShelveDtos(shelves);
    }

    public Page<RowWithProducts> findFreePlaceForProduct(ProductFreePlaceDto freePlaceDto) {
        workerWarehouseService.validateWorkerWarehouseRelation(freePlaceDto.warehouseIds());
        double volume = freePlaceDto.amount()*freePlaceDto.height()*freePlaceDto.length()*freePlaceDto.width();
        List<ShelveTier> tiers = shelveTierService.findFreePlace(volume, freePlaceDto.warehouseIds());
        List<RowWithProducts> shelfWithProductsDtos = productWarehouseService.getShelfWithProducts(tiers);
        return  new PageImpl<>(shelfWithProductsDtos, Pageable.unpaged(), shelfWithProductsDtos.size());
    }

    public void removeShelve(UUID shelveId) {
        Shelve shelve = shelveRepository.findById(shelveId).orElseThrow(() -> new IllegalArgumentException("Shelve with id " + shelveId + " does not exist"));
        shelveTierService.removeShelfTiers(shelve.getShelveTiers());
        shelveRepository.deleteById(shelve.getId());
    }

    public Page<Shelve> getShelvesFromWarehouse(UUID warehouseId, Pageable  pageable) {
        return shelveRepository.findAllByWarehouseIdOrderByRowAscNumberAsc(warehouseId, pageable);
    }

    @Transactional
    public ShelveDto updateShelf(UUID warehouseId, ShelveDto shelveDto) {
        workerWarehouseService.validateWorkerWarehouseRelation(workerIdSupplier.getUserId(), warehouseId);
        Shelve shelve = shelveRepository.findById(shelveDto.getId()).orElseThrow(() -> new IllegalArgumentException("Shelve with id " + shelveDto.getId() + " does not exist"));
        Shelve updatedShelve = ShelveMapper.toShelve(shelveDto, shelve.getWarehouse());
        shelve.modifyShelve(updatedShelve);
        updateTiers(shelve, updatedShelve);
        return ShelveMapper.toShelveDto(shelve);
    }
    public void updateTiers(Shelve shelve, Shelve updatedShelve){
        Set<ShelveTier> tiers = shelve.getShelveTiers();
        Set<ShelveTier> updatedTiers =  updatedShelve.getShelveTiers();
        removeTiers(tiers, updatedTiers);
        for (ShelveTier updateTier : updatedTiers) {
            ShelveTier existingTier = tiers.stream()
                    .filter(t -> t.getId().equals(updateTier.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingTier != null) {
                existingTier.updateTier(updateTier);
            } else {
                updateTier.setShelve(shelve);
                shelveTierService.saveShelfTier(updateTier);
            }
        }
    }

    private void removeTiers(Set<ShelveTier> existingTiers, Set<ShelveTier> updatedTiers){
        Set<UUID> updatedTiersIds = updatedTiers.stream().map(ShelveTier::getId).collect(Collectors.toSet());
        existingTiers.stream().map(ShelveTier::getId).filter(tier -> !updatedTiersIds.contains(tier)).forEach(shelveTierService::removeShelfTier);
    }

    public Optional<Shelve> findShelfWithTier(UUID warehouseId, Integer row, Integer shelfNumber, Integer tier) {
        Optional<Shelve> optionalShelf = shelveRepository.findByWarehouseIdAndRowAndNumber(warehouseId, row, shelfNumber);
       if(optionalShelf.isEmpty())
           return optionalShelf;
        Shelve shelf = optionalShelf.get();
        shelf.getShelveTiers().stream()
                .filter(t -> t.getNumber()== tier)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Shelve with tier " + tier + " does not exist"));
        return optionalShelf;
    }
}
