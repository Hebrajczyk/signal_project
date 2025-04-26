package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

/**
 * Strategy for checking rapid saturation drop within 10 minutes.
 */
public class SaturationDropStrategy implements AlertStrategy {

    private static final String LABEL = "OxygenSaturation";
    private static final long WINDOW = 10 * 60 * 1000; // 10 minutes in ms
    private static final double DROP_THRESHOLD = 5.0;

    @Override
    public boolean checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());

        PatientRecord latest = null;

        for (int i = 0; i < records.size(); i++) {
            PatientRecord r = records.get(i);
            if (r.getRecordType().equals(LABEL)) {
                if (latest == null || r.getTimestamp() > latest.getTimestamp()) {
                    latest = r;
                }
            }
        }

        if (latest == null) {
            return false;
        }

        long windowStart = latest.getTimestamp() - WINDOW;

        for (int i = 0; i < records.size(); i++) {
            PatientRecord r = records.get(i);
            if (r.getRecordType().equals(LABEL) &&
                    r.getTimestamp() >= windowStart &&
                    r.getTimestamp() < latest.getTimestamp()) {

                double diff = r.getMeasurementValue() - latest.getMeasurementValue();
                if (diff >= DROP_THRESHOLD) {
                    return true;
                }
            }
        }

        return false;
    }
}
