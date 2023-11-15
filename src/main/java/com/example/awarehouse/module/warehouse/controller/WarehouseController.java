package com.example.awarehouse.module.warehouse.controller;

import com.example.awarehouse.module.warehouse.WarehouseService;
import com.example.awarehouse.module.warehouse.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.awarehouse.util.Constants.URI_VERSION_V1;
import static com.example.awarehouse.util.Constants.URI_WAREHOUSE;

@RestController
@RequestMapping(URI_VERSION_V1+URI_WAREHOUSE)
public class WarehouseController {
    WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PutMapping
    public void updateWarehouse(@RequestBody WarehouseRequest warehouseRequest){
        warehouseService.updateWarehouse(warehouseRequest);

    }

    @PostMapping
    public ResponseEntity<WarehouseResponseDto> createWarehouse(@Valid @RequestBody  WarehouseCreation warehouseCreation){
        WarehouseResponseDto warehouseResponseDto = warehouseService.createWarehouse(warehouseCreation);
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<BasicWarehouseInfoDto>> getWarehouses(){
        List<BasicWarehouseInfoDto> warehouses = warehouseService.getWarehouses();
        return ResponseEntity.status(HttpStatus.OK).body(warehouses);
    }

    @PostMapping("/{groupId}")
    ResponseEntity<HttpStatus> addWarehouseToGroup(@PathVariable Long groupId, @RequestBody WarehouseIdDto warehouseIdDto){
        warehouseService.addWarehouseToGroup(groupId, warehouseIdDto);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }
}
