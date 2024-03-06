package com.example.awarehouse.module.administration.dto;

import com.example.awarehouse.module.warehouse.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class AdminWorkersDto {
    private final String name;
    private  final UUID id;
    private List<Worker> workers;

        public record Worker( UUID id, UUID workerEntityId, String name, String surname, String role) {
    }

}
