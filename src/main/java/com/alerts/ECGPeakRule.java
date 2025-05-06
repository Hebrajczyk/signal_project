package com.alerts;

import com.data_management.Patient;

/**
 * ECGPeakRule now uses a strategy to check the alert condition
 */
public class ECGPeakRule implements AlertRule {
    private final AlertStrategy strategy;

    public ECGPeakRule(AlertStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean check(Patient patient) {
        return strategy.checkAlert(patient);
    }

    @Override
    public String getConditionName() {
        return "ECG peak condition";
    }
}
