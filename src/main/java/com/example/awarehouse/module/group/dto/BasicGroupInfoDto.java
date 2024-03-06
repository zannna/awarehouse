package com.example.awarehouse.module.group.dto;

import com.example.awarehouse.module.warehouse.dto.BasicInfo;

import java.util.UUID;

public record BasicGroupInfoDto(UUID id, String name, Boolean isWorkerAdmin) implements BasicInfo {
}
