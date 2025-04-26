package com.alerts;

import com.data_management.Patient;

/**
 * SaturationDropRule now uses a strategy to check the alert condition.
 */
public class SaturationDropRule implements AlertRule {
    private final AlertStrategy strategy;

    public SaturationDropRule(AlertStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean check(Patient patient) {
        return strategy.checkAlert(patient);
    }

    @Override
    public String getConditionName() {
        return "Saturation drop";
    }
}
