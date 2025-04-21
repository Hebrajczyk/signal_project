package com.alerts;

/**
 * Handles triggered alerts.
 * Responsible for logging, dispatching, or escalating alerts in the system.
 */
public class AlertManager {

    /**
     * Dispatches an alert.
     * Currently, it logs the alert to the console.
     *
     * @param alert the alert to dispatch
     */
    public void dispatchAlert(Alert alert) {
        System.out.println(
                "[ALERT] Patient ID: " + alert.getPatientId() +
                        " | Condition: " + alert.getCondition() +
                        " | Timestamp: " + alert.getTimestamp()
        );
    }
}
