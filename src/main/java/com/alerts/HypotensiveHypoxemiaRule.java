package com.alerts;

import com.data_management.Patient;

/**
 * HypotensiveHypoxemiaRule now uses a strategy to check the alert condition.
 */
public class HypotensiveHypoxemiaRule implements AlertRule {
    private final AlertStrategy strategy;

    public HypotensiveHypoxemiaRule(AlertStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean check(Patient patient) {
        return strategy.checkAlert(patient);
    }

    @Override
    public String getConditionName() {
        return "Hypotensive hypoxemia condition";
    }
}
