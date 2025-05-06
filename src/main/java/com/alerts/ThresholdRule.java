package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * Triggers an alert if the latest measurement of a specific type
 * is above or below a defined threshold
 */
public class ThresholdRule implements AlertRule {
    private AlertStrategy strategy;

    public ThresholdRule(AlertStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean check(Patient patient) {
        return strategy.checkAlert(patient);
    }

    @Override
    public String getConditionName() {
        return strategy.getClass().getSimpleName();
    }
}

