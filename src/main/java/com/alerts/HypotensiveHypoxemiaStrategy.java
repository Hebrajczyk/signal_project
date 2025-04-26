package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

/**
 * Strategy for detecting low blood pressure and low oxygen saturation together.
 */
public class HypotensiveHypoxemiaStrategy implements AlertStrategy {

    private static final String BP_LABEL = "SystolicBloodPressure";
    private static final String O2_LABEL = "OxygenSaturation";
    private static final double BP_THRESHOLD = 90.0;
    private static final double O2_THRESHOLD = 92.0;

    @Override
    public boolean checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());

        PatientRecord latestBP = null;
        PatientRecord latestO2 = null;

        for (int i = 0; i < records.size(); i++) {
            PatientRecord r = records.get(i);
            if (r.getRecordType().equals(BP_LABEL)) {
                if (latestBP == null || r.getTimestamp() > latestBP.getTimestamp()) {
                    latestBP = r;
                }
            } else if (r.getRecordType().equals(O2_LABEL)) {
                if (latestO2 == null || r.getTimestamp() > latestO2.getTimestamp()) {
                    latestO2 = r;
                }
            }
        }

        if (latestBP == null || latestO2 == null) {
            return false;
        }

        return (latestBP.getMeasurementValue() <= BP_THRESHOLD) && (latestO2.getMeasurementValue() <= O2_THRESHOLD);
    }
}
