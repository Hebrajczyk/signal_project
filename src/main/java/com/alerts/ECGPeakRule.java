package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * Triggers an alert if the latest ECG measurement is greater than the average
 * of all previous ECG measurements for the patient.
 */
public class ECGPeakRule implements AlertRule {

    private static final String LABEL = "ECG";

    @Override
    public boolean check(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());

        double sum = 0.0;
        int count = 0;
        PatientRecord latest = null;

        for (int i = 0; i < records.size(); i++) {
            PatientRecord r = records.get(i);
            if (r.getRecordType().equals(LABEL)) {
                if (latest == null || r.getTimestamp() > latest.getTimestamp()) {
                    latest = r;
                }
            }
        }

        if (latest == null) return false;

        long latestTimestamp = latest.getTimestamp();

        for (int i = 0; i < records.size(); i++) {
            PatientRecord r = records.get(i);
            if (r.getRecordType().equals(LABEL) && r.getTimestamp() < latestTimestamp) {
                sum += r.getMeasurementValue();
                count++;
            }
        }

        if (count == 0) return false;

        double average = sum / count;
        return latest.getMeasurementValue() > average;
    }

    @Override
    public String getConditionName() {
        return "ECG peak (above patient average)";
    }
}
