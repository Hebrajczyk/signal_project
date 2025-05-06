package com.alerts;

/**
 * Factory for creating blood oxygen alerts
 */
public class BloodOxygenAlertFactory extends AlertFactory {

    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new Alert(patientId, "Blood Oxygen Alert: " + condition, timestamp);
    }
}
