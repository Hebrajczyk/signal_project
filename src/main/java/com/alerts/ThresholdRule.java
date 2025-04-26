package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * Triggers an alert if the latest measurement of a specific type
 * is above or below a defined threshold.
 */
public class ThresholdRule implements AlertRule {

    private final String label;
    private final double threshold;
    private final boolean above;

    public ThresholdRule(String label, double threshold, boolean above) {
        this.label = label;
        this.threshold = threshold;
        this.above = above;
    }

    @Override
    public boolean check(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());

        PatientRecord latest = null;

        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);

            if (record.getRecordType().equals(label)) {
                if (latest == null) {
                    latest = record;
                } else {
                    if (record.getTimestamp() > latest.getTimestamp()) {
                        latest = record;
                    }
                }
            }
        }

        if (latest == null) {
            return false;
        }

        double value = latest.getMeasurementValue();
        if (above) {
            return value > threshold;
        } else {
            return value < threshold;
        }
    }

    @Override
    public String getConditionName() {
        String condition;
        if (above) {
            condition = " > ";
        } else {
            condition = " < ";
        }

        return label + condition + threshold;
    }
}
