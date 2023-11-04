package com.example.awarehouse.module.warehouse.controller;

import com.example.awarehouse.module.warehouse.WarehouseService;
import com.example.awarehouse.module.warehouse.dto.WarehouseCreation;
import com.example.awarehouse.module.warehouse.dto.WarehouseListResponseDto;
import com.example.awarehouse.module.warehouse.dto.WarehouseRequest;
import com.example.awarehouse.module.warehouse.dto.WarehouseResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<List<WarehouseListResponseDto>> getWarehouses(){
        List<WarehouseListResponseDto> warehouses = warehouseService.getWarehouses();
        return ResponseEntity.status(HttpStatus.OK).body(warehouses);
    }


//    @GetMapping
//    public void cos(){
//        System.out.println("kotek");
//        Jwt token= (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String firstName = token.getClaim("given_name");
//        String lastName =  token.getClaim("family_name");
//
//       // String userId = keycloakPrincipal.getName(); // This is the user's I
//    }
}
