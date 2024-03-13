package com.example.awarehouse.module.product.dto;

public enum ProductWarehouseFilterField {
    ID("product.id", "uuid"),
    WAREHOUSE("warehouse.name", "string"),
    ROW("tier.shelve.row", "number"),
    SHELF("tier.shelve.number", "number"),
    TIER("tier.number", "number"),
    NAME("product.title", "string"),
    AMOUNT("numberOfProducts", "number"),
    PRICE("product.price.amount", "number");

    private final String path;
    private final String dataType;

    ProductWarehouseFilterField(String path, String dataType) {
        this.path = path;
        this.dataType = dataType;
    }

    public String getPath() {
        return this.path;
    }

    public String getDataType() {
        return this.dataType;
    }

    public static ProductWarehouseFilterField fromString(String field) {
        for (ProductWarehouseFilterField filterField : values()) {
            if (filterField.name().equalsIgnoreCase(field) || filterField.getPath().equalsIgnoreCase(field)) {
                return filterField;
            }
        }
        throw new IllegalArgumentException("No constant with text " + field + " found");
    }

    public static String toColumnPath(ProductWarehouseFilterField field) {
        return field.getPath();
    }
}
