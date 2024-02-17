package com.example.awarehouse.module.warehouse.util.unit;

public enum ImperialUnit implements  UnitEnum {
    INCH(1.0 / 12.0),
    FOOT(1.0),
    YARD(3.0);

    private final double multiplier;

    ImperialUnit(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }
}