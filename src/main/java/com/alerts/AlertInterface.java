package com.alerts;

/**
 * Interface for alerts and alert decorators.
 */
public interface AlertInterface {
    String getPatientId();
    String getCondition();
    long getTimestamp();
}
