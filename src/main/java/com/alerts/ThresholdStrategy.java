package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

public class ThresholdStrategy implements AlertStrategy {

    private final String label;
    private final double threshold;
    private final boolean above;

    public ThresholdStrategy(String label, double threshold, boolean above) {
        this.label = label;
        this.threshold = threshold;
        this.above = above;
    }

    @Override
    public boolean checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());

        PatientRecord latest = null;

        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);

            if (record.getRecordType().equals(label)) {
                if (latest == null || record.getTimestamp() > latest.getTimestamp()) {
                    latest = record;
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
}
