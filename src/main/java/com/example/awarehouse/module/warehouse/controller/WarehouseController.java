package com.example.awarehouse.module.warehouse.controller;

import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.warehouse.WarehouseService;
import com.example.awarehouse.module.warehouse.WorkerWarehouseRepository;
import com.example.awarehouse.module.warehouse.dto.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.example.awarehouse.util.Constants.URI_VERSION_V1;
import static com.example.awarehouse.util.Constants.URI_WAREHOUSE;

@RestController
@RequestMapping(URI_VERSION_V1+URI_WAREHOUSE)
@AllArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final WorkerWarehouseRepository workerWarehouseRepository;

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
    ResponseEntity<HttpStatus> addWarehouseToGroup(@PathVariable UUID groupId, @RequestBody WarehouseIdDto warehouseIdDto){
        warehouseService.addWarehouseToGroup(groupId, warehouseIdDto);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{warehouseId}/group")
    public ResponseEntity<Map<BasicGroupInfoDto, Set<BasicWarehouseInfoDto>>> getGroupsAssociatedWithWarehouse(@PathVariable UUID warehouseId){
        Map<BasicGroupInfoDto, Set<BasicWarehouseInfoDto>> groupWithWarehouses = warehouseService.getGroupsAssociatedWithWarehouse(warehouseId);
        return ResponseEntity.status(HttpStatus.OK).body(groupWithWarehouses);
    }
}
