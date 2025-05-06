package com.alerts;

/**
 * Abstract factory for creating Alert objects
 */
public abstract class AlertFactory {

    public abstract Alert createAlert(String patientId, String condition, long timestamp);

}
