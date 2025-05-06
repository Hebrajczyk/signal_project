package com.alerts;

import com.data_management.Patient;

/**
 * An interface for defining alert conditions
 * Each rule decides whether a patients data meets specific criteria
 */
public interface AlertRule {

    /**
     * Evaluates the given patient's data and returns true if an alert condition is met.
     *
     * @param patient the patient whose data is being evaluated
     * @return true if an alert should be triggered
     */
    boolean check(Patient patient);

    /**
     * Returns the name or description of the condition associated with this rule
     *
     * @return a string describing the condition
     */
    String getConditionName();
}
