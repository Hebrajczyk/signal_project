package com.alerts;

import com.data_management.Patient;
import com.data_management.DataStorage;
import java.util.*;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private final DataStorage dataStorage;
    private final AlertManager alertManager;
    private final Map<Integer, List<AlertRule>> patientRules;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     *
     * @param dataStorage the data storage system that provides access to patient data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alertManager = new AlertManager();
        this.patientRules = new HashMap<>();
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
        // I implemented AlertManager
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
     * are met. If a condition is met, an alert is triggered via the alert manager.
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
                Alert alert = new Alert(String.valueOf(id), rule.getConditionName(), System.currentTimeMillis());
                alertManager.dispatchAlert(alert);
            }
        }
    }
}
