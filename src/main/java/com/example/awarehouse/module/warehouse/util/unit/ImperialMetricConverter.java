package com.example.awarehouse.module.warehouse.util.unit;

import com.example.awarehouse.module.warehouse.util.unit.MetricConverter;

public class ImperialMetricConverter implements MetricConverter {
    @Override
    public double convert(String prefix, double value) {
        ImperialUnit imperialUnit = ImperialUnit.valueOf(prefix);
        return value * imperialUnit.getMultiplier();
    }
}
