package com.alerts;

import com.data_management.Patient;
import com.data_management.DataStorage;
import java.util.*;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met.
 * It now uses the Factory Method pattern to create alerts dynamically.
 */
public class AlertGenerator {
    private final DataStorage dataStorage;
    private AlertManager alertManager;
    private final Map<Integer, List<AlertRule>> patientRules;
    private final AlertFactory alertFactory;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}
     * and an {@code AlertFactory}
     *
     * @param dataStorage the data storage system that provides access to patient data
     * @param alertFactory the factory used to create alert objects
     */
    public AlertGenerator(DataStorage dataStorage, AlertFactory alertFactory) {
        this.dataStorage = dataStorage;
        this.alertManager = new AlertManager();
        this.patientRules = new HashMap<>();
        this.alertFactory = alertFactory;
    }

    /**
     * Triggers an alert for the monitoring system
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
        alertManager.dispatchAlert(alert);
    }

    /**
     * Adds a rule for a specific patient.
     *
     * @param patientId the ID of the patient
     * @param rule the alert rule to add
     */
    public void addRule(int patientId, AlertRule rule) {
        List<AlertRule> rules = patientRules.get(patientId);
        if (rules == null) {
            rules = new ArrayList<AlertRule>();
            patientRules.put(patientId, rules);
        }
        rules.add(rule);
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        int id = patient.getPatientId();
        List<AlertRule> rules = patientRules.get(id);

        if (rules == null) {
            return;
        }

        for (int i = 0; i < rules.size(); i++) {
            AlertRule rule = rules.get(i);
            if (rule.check(patient)) {
                Alert alert = alertFactory.createAlert(
                        String.valueOf(id),
                        rule.getConditionName(),
                        System.currentTimeMillis()
                );
                triggerAlert(alert);
            }
        }
    }

    public void setAlertManager(AlertManager manager) {
        this.alertManager = manager;
    }

}
