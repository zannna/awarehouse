package com.example.awarehouse.module.product.dto;

public enum ProductFilterField {
    ID("product.id", "uuid"),
    TIER("tier.number", "number"),
    NAME("product.title", "string"),
    AMOUNT("numberOfProducts", "number"),
    PRICE("product.price.amount", "number");

    private final String path;
    private final String dataType;

    ProductFilterField(String path, String dataType) {
        this.path = path;
        this.dataType = dataType;
    }

    public String getPath() {
        return this.path;
    }

    public String getDataType() {
        return this.dataType;
    }

    public static ProductFilterField fromString(String field) {
        for (ProductFilterField filterField : values()) {
            if (filterField.name().equalsIgnoreCase(field) || filterField.getPath().equalsIgnoreCase(field)) {
                return filterField;
            }
        }
        throw new IllegalArgumentException("Unsupported filter  " + field );
    }

    public static String toColumnPath(ProductFilterField field) {
        return field.getPath();
    }
}
