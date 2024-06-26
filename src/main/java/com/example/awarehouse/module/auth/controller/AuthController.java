package com.example.awarehouse.module.auth.controller;

import com.example.awarehouse.module.administration.AdministrationManagement;
import com.example.awarehouse.module.auth.KeycloakService;
import com.example.awarehouse.module.auth.WorkerService;
import com.example.awarehouse.module.administration.dto.AdminWorkersDto;
import com.example.awarehouse.module.auth.dto.LoginDto;
import com.example.awarehouse.module.auth.dto.UserCreationDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

import static com.example.awarehouse.util.Constants.*;

@Controller
@RequestMapping(URI_VERSION_V1+URI_AUTH)
@AllArgsConstructor
public class AuthController {
    WorkerService workerService;
    KeycloakService keycloakService;
    AdministrationManagement administrationManagement;
    @GetMapping
    void register() {
      workerService.register();
    }

    @PostMapping("/register")
    ResponseEntity<HttpStatus> registerUser(@RequestBody UserCreationDto userCreationDto) {
        UUID userId = keycloakService.createUserInKeycloak(userCreationDto);
        workerService.registerWorker(userCreationDto,userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
