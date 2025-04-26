package com.alerts;

import com.data_management.Patient;

/**
 * SystolicBloodPressureRule now uses a strategy to check the alert condition.
 */
public class SystolicBloodPressureRule implements AlertRule {
    private final AlertStrategy strategy;

    public SystolicBloodPressureRule(AlertStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean check(Patient patient) {
        return strategy.checkAlert(patient);
    }

    @Override
    public String getConditionName() {
        return "Systolic blood pressure";
    }
}
