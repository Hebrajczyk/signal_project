package com.alerts;

import com.data_management.Patient;

/**
 * Defines the interface for checking alert conditions.
 */
public interface AlertStrategy {
    boolean checkAlert(Patient patient);
}
