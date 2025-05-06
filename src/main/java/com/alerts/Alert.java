package com.alerts;

/**
 * Represents a single alert
 *
 */
public class Alert implements AlertInterface {


    private String patientId;


    private String condition;


    private long timestamp;

    /**
     * Creates a new alert with the given patient ID, condition, and timestamp
     * This object is later passed around the system
     *
     * @param patientId the ID of the patient
     * @param condition short description of the condition
     * @param timestamp time the alert was triggered
     */
    public Alert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }


    public String getPatientId() {
        return patientId;
    }


    public String getCondition() {
        return condition;
    }


    public long getTimestamp() {
        return timestamp;
    }
}
