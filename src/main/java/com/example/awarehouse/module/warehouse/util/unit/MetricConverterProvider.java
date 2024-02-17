package com.example.awarehouse.module.warehouse.util.unit;

public class MetricConverterProvider {
    private static final SiMetricConverter siMetrixCoverter = new SiMetricConverter();
    private static final ImperialMetricConverter imperialMetricConverter = new ImperialMetricConverter();
    public static MetricConverter getConverter(LengthUnit unit) {
        if (unit == LengthUnit.METER) {
            return siMetrixCoverter;
        } else if (unit == LengthUnit.FOOT) {
            return imperialMetricConverter;
        }
        throw new IllegalArgumentException("Unknown unit: " + unit);
    }
}
