package com.example.awarehouse.module.token.controller;

import com.example.awarehouse.module.token.SharingTokenService;
import com.example.awarehouse.module.token.dto.SharingTokenResponse;
import com.example.awarehouse.module.token.dto.WarehouseId;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.example.awarehouse.util.Constants.*;

@RestController
@RequestMapping(URI_VERSION_V1+URI_SHARING_TOKEN)
@AllArgsConstructor
public class SharingTokenController {
    SharingTokenService sharingTokenService;

    @GetMapping( URI_WAREHOUSE+"/{warehouseId}")
    public ResponseEntity<SharingTokenResponse> getSharingToken(@PathVariable UUID warehouseId){
        SharingTokenResponse sharingToken = sharingTokenService.getSharingToken(warehouseId);
        return  ResponseEntity.status(HttpStatus.OK).body(sharingToken);
    }

    @PostMapping("/{sharingTokenId}")
    public ResponseEntity<WarehouseId> joinWarehouse(@PathVariable String sharingTokenId){
        WarehouseId warehouseId = sharingTokenService.joinWarehouse(sharingTokenId);
        return ResponseEntity.status(HttpStatus.OK).body(warehouseId);
    }


}
