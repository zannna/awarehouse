package com.example.awarehouse.module.group.dto;

import jakarta.validation.constraints.NotBlank;

public record GroupRequest(
    @NotBlank
    String name){
}
