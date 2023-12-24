package com.example.awarehouse.module.product.dto;

import java.util.List;
import java.util.UUID;

public record UnderstockedProductInGroupDto(UUID id, String name, String price, String warehouses){
}
