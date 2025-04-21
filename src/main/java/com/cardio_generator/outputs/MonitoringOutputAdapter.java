package com.cardio_generator.outputs;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;

public class MonitoringOutputAdapter implements OutputStrategy {

    private final DataStorage dataStorage;
    private final AlertGenerator alertGenerator;

    public MonitoringOutputAdapter(DataStorage dataStorage, AlertGenerator alertGenerator) {
        this.dataStorage = dataStorage;
        this.alertGenerator = alertGenerator;
    }

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Convert incoming data to a double value
            double value = parseDouble(data);

            // Save data to storage
            dataStorage.addPatientData(patientId, value, label, timestamp);

            // Evaluate alerts for this patient
            Patient patient = dataStorage.getPatientById(patientId);
            if (patient != null) {
                alertGenerator.evaluateData(patient);
            }
        } catch (NumberFormatException e) {
            System.err.println("Could not parse data for patient " + patientId + ": " + data);
        }
    }

    // Helper method: allows support for "triggered", "resolved" as well as numeric
    private double parseDouble(String input) {
        switch (input.trim().toLowerCase()) {
            case "triggered":
                return 1.0;
            case "resolved":
                return 0.0;
            default:
                return Double.parseDouble(input.trim());
        }
    }
}
