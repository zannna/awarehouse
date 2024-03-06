package com.example.awarehouse.module.warehouse.dto;

import java.util.UUID;

public record BasicWarehouseInfoDto(UUID id, String name, Boolean isWorkerAdmin) implements BasicInfo {
}
