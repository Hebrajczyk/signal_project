package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

/**
 * Strategy for detecting ECG peaks based on recent measurements.
 */
public class ECGPeakStrategy implements AlertStrategy {

    private static final String LABEL = "ECG";

    @Override
    public boolean checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());

        List<Double> values = new java.util.ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            PatientRecord r = records.get(i);
            if (r.getRecordType().equals(LABEL)) {
                values.add(r.getMeasurementValue());
            }
        }

        if (values.size() < 3) {
            return false;
        }

        double previousAverage = (values.get(values.size() - 2) + values.get(values.size() - 3)) / 2.0;
        double latestValue = values.get(values.size() - 1);

        return latestValue > previousAverage;
    }
}
