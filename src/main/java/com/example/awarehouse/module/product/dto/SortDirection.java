package com.example.awarehouse.module.product.dto;

public enum SortDirection {
    ASC,
    DESC;

    public static SortDirection fromString(String direction) {
        for (SortDirection sortDirection : values()) {
            if (sortDirection.name().equalsIgnoreCase(direction)) {
                return sortDirection;
            }
        }
        throw new IllegalArgumentException("No constant with text " + direction + " found");
    }

}
