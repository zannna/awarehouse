package com.example.awarehouse.module.warehouse.controller;

import com.example.awarehouse.module.group.dto.BasicGroupInfoDto;
import com.example.awarehouse.module.group.dto.GroupWithWarehouses;
import com.example.awarehouse.module.warehouse.WarehouseService;
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

import static com.example.awarehouse.util.Constants.*;

@RestController
@RequestMapping(URI_VERSION_V1+URI_WAREHOUSE)
@AllArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

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

    @GetMapping("/{warehouseId}/row")
    public int getWarehouseNumberOfRows(@PathVariable UUID warehouseId){
        return warehouseService.getWarehouseNumberOfRows(warehouseId);
    }

    @PostMapping("/{warehouseId}"+URI_GROUP)
    ResponseEntity<HttpStatus> addGroupToWarehouse(@RequestBody BasicGroupInfoDto group, @PathVariable UUID warehouseId){
        warehouseService.addWarehouseToGroup(group.id(), warehouseId);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{warehouseId}"+URI_GROUP+"/{groupId}")
    ResponseEntity<HttpStatus> removeGroupFromWarehouse(@PathVariable UUID warehouseId, @PathVariable UUID groupId){
        warehouseService.removeGroupFromWarehouse(warehouseId, groupId);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{warehouseId}/group")
    public ResponseEntity<Map<BasicGroupInfoDto, Set<BasicWarehouseInfoDto>>> getGroupsAssociatedWithWarehouse(@PathVariable UUID warehouseId){
        Map<BasicGroupInfoDto, Set<BasicWarehouseInfoDto>> groupWithWarehouses = warehouseService.getGroupsAssociatedWithWarehouse(warehouseId);
        return ResponseEntity.status(HttpStatus.OK).body(groupWithWarehouses);
    }

    @PutMapping("/{warehouseId}/row")
    ResponseEntity<HttpStatus> updateWarehouseNumberOfRows(@PathVariable UUID warehouseId,  @RequestParam(name = "rowsNumber")  Integer rowsNumber){
        warehouseService.updateWarehouseNumberOfRows(warehouseId, rowsNumber);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(URI_GROUP)
    ResponseEntity<GroupAndWarehouses> getAllGroupsWithWarehouses(){
        GroupAndWarehouses groups =warehouseService.getAllGroupsWithWarehouses();
        return  ResponseEntity.status(HttpStatus.OK).body(groups);
    }


}
