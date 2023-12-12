package com.example.awarehouse.module.warehouse.shelve.controller;

import com.example.awarehouse.module.warehouse.shelve.ShelveService;
import com.example.awarehouse.module.warehouse.shelve.dto.ShelveCreationDto;
import com.example.awarehouse.module.warehouse.shelve.dto.ShelveDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.example.awarehouse.util.Constants.*;

@RestController
@RequestMapping(URI_VERSION_V1+URI_WAREHOUSE+"/{warehouseId}"+URI_SHELVE)
@AllArgsConstructor
public class ShelveController {
    private final ShelveService shelveService;

    @PostMapping
    public ResponseEntity<ShelveDto> createShelve(@PathVariable UUID warehouseId, @RequestBody ShelveCreationDto shelveDto) {
        return ResponseEntity.status(HttpStatus.OK).body(shelveService.createShelve(warehouseId, shelveDto));
    }
}
