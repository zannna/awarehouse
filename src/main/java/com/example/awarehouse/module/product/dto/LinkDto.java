package com.example.awarehouse.module.product.dto;

import jakarta.validation.constraints.Pattern;

import java.util.UUID;

import static com.example.awarehouse.module.product.util.ProductConstants.LINK_HAS_INVALID_FORMAT;
import static com.example.awarehouse.module.product.util.ProductConstants.LINK_REGEX;

public record LinkDto(
        @Pattern(regexp = LINK_REGEX, message = LINK_HAS_INVALID_FORMAT) String link,
        UUID associateElementId){
}
