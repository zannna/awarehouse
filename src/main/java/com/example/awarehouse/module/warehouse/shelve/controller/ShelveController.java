package com.example.awarehouse.module.warehouse.shelve.controller;

import com.example.awarehouse.module.product.dto.ProductFreePlaceDto;
import com.example.awarehouse.module.product.dto.RowWithProducts;
import com.example.awarehouse.module.warehouse.shelve.ShelveService;
import com.example.awarehouse.module.warehouse.shelve.dto.ShelfCreationDto;
import com.example.awarehouse.module.warehouse.shelve.dto.ShelveDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.example.awarehouse.util.Constants.*;

@RestController
@RequestMapping(URI_VERSION_V1+URI_WAREHOUSE+"/{warehouseId}"+ URI_SHELF)
@AllArgsConstructor
public class ShelveController {
    private final ShelveService shelveService;

    @PostMapping
    public ResponseEntity<ShelveDto> createShelve(@PathVariable UUID warehouseId, @RequestBody ShelfCreationDto shelveDto) {
        return ResponseEntity.status(HttpStatus.OK).body(shelveService.createShelve(warehouseId, shelveDto));
    }

    @PutMapping
    public ResponseEntity<ShelveDto> updateShelve(@PathVariable UUID warehouseId, @RequestBody ShelveDto shelveDto) {
        return ResponseEntity.status(HttpStatus.OK).body(shelveService.updateShelf(warehouseId, shelveDto));
    }

    @GetMapping
    public ResponseEntity<List<ShelveDto>> getWarehouseInventory(@PathVariable UUID warehouseId) {
        return ResponseEntity.status(HttpStatus.OK).body(shelveService.getShelves(warehouseId));
    }

    @PostMapping(URI_FREE_PLACE)
    public Page<RowWithProducts> findFreePlaceForProduct(@RequestBody ProductFreePlaceDto freePlaceDto) {
        return shelveService.findFreePlaceForProduct(freePlaceDto);
    }

    @DeleteMapping("/{shelveId}")
    public ResponseEntity<HttpStatus> deleteShelve(@PathVariable UUID shelveId){
        shelveService.removeShelve(shelveId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
