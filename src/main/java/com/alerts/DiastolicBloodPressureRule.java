package com.alerts;

import com.data_management.Patient;

/**
 * DiastolicBloodPressureRule now uses a strategy to check the alert condition
 */
public class DiastolicBloodPressureRule implements AlertRule {
    private final AlertStrategy strategy;

    public DiastolicBloodPressureRule(AlertStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean check(Patient patient) {
        return strategy.checkAlert(patient);
    }

    @Override
    public String getConditionName() {
        return "Diastolic blood pressure";
    }
}
