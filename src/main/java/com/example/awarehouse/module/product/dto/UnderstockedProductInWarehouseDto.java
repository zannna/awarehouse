package com.example.awarehouse.module.product.dto;

import java.util.UUID;

public record UnderstockedProductInWarehouseDto(UUID id, String name, String price, String group){
}
