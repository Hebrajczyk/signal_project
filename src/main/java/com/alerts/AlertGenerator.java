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
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alertManager = new AlertManager();
        this.patientRules = new HashMap<>();
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
        //I implemented AlertManager
    }


    public void addRule(int patientId, AlertRule rule) {
        patientRules.computeIfAbsent(patientId, k -> new ArrayList<>()).add(rule);
    }


    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        int id = patient.getPatientId();
        List<AlertRule> rules = patientRules.get(id);

        if (rules == null) return;

        for (AlertRule rule : rules) {
            if (rule.check(patient)) {
                Alert alert = new Alert(String.valueOf(id), rule.getConditionName(), System.currentTimeMillis());
                alertManager.dispatchAlert(alert);
            }
        }
    }

}
