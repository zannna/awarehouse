package com.example.awarehouse.module.token.controller;

import com.example.awarehouse.module.token.SharingTokenService;
import com.example.awarehouse.module.token.dto.SharingTokenDto;
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

    @GetMapping("/{ownerId}")
    public ResponseEntity<SharingTokenDto> getSharingToken(@PathVariable UUID ownerId){
        SharingTokenDto sharingToken = sharingTokenService.getSharingToken(ownerId);
        return  ResponseEntity.status(HttpStatus.OK).body(sharingToken);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> joinWarehouse(@RequestBody SharingTokenDto sharingTokenDto){
        sharingTokenService.joinWarehouse( sharingTokenDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
