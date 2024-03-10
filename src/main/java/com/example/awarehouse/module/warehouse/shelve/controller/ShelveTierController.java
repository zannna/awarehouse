package com.example.awarehouse.module.warehouse.shelve.controller;

import com.example.awarehouse.module.warehouse.shelve.tier.ShelveTierService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.example.awarehouse.util.Constants.*;

@RestController
@RequestMapping(URI_VERSION_V1+URI_WAREHOUSE+"/{warehouseId}"+ URI_SHELF+"/{shelveId}"+URI_TIER+ "/{tierId}")
@AllArgsConstructor
public class ShelveTierController {
    private final ShelveTierService shelveTierService;

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteTier(@PathVariable UUID tierId){
        shelveTierService.removeShelfTier(tierId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
