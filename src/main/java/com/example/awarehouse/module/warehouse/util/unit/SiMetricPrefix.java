package com.example.awarehouse.module.warehouse.util.unit;

public enum SiMetricPrefix implements  UnitEnum {
    METER(1),
    DECY(0.1),
    CENTY(0.01),
    MILI(0.001);

    private final double multiplier;

    SiMetricPrefix(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
