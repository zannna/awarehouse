package com.example.awarehouse.module.product.dto;

import java.util.UUID;

public record ProductWithGroupDto (UUID id, String title, PriceDto price, String groupName ){
}
