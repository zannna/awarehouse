package com.example.awarehouse.module.administration.controller;

import com.example.awarehouse.module.administration.AdministrationManagement;
import com.example.awarehouse.module.administration.dto.AdminWorkersDto;
import com.example.awarehouse.module.administration.dto.UpdateRoleDto;
import com.example.awarehouse.module.warehouse.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.example.awarehouse.util.Constants.URI_ADMIN;
import static com.example.awarehouse.util.Constants.URI_VERSION_V1;

@RestController
@RequestMapping(URI_VERSION_V1 + URI_ADMIN)
@AllArgsConstructor
public class AdministrationController {
    private final AdministrationManagement administrationManagement;
    @GetMapping
    ResponseEntity<List<AdminWorkersDto>> getAdminWarehousesAndGroups(){
        List<AdminWorkersDto> warehouses = administrationManagement.getAdminData();
        return ResponseEntity.status(HttpStatus.OK).body(warehouses);
    }

    @PatchMapping("/{id}")
    ResponseEntity<HttpStatus> updateRole(@RequestBody UpdateRoleDto updateRoleDto, @PathVariable UUID id){
        administrationManagement.updateRole(updateRoleDto, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{workerEntityIds}")
    ResponseEntity<HttpStatus> deleteWorkerEntity(@PathVariable List<UUID> workerEntityIds){
        administrationManagement.deleteWorkerEntity(workerEntityIds);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
