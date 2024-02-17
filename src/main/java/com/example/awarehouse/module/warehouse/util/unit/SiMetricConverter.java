package com.example.awarehouse.module.warehouse.util.unit;

public class SiMetricConverter implements MetricConverter {

    @Override
    public double convert(String prefix, double value) {
        SiMetricPrefix siMetricPrefix =  SiMetricPrefix.valueOf(prefix);
        return value * siMetricPrefix.getMultiplier();
    }

}
