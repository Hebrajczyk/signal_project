package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * Triggers an alert if the patient's most recent systolic blood pressure is ≤ 90
 * and the most recent oxygen saturation is ≤ 92 at the same time.
 */
public class HypotensiveHypoxemiaRule implements AlertRule {

    private static final String BP_LABEL = "SystolicBloodPressure";
    private static final String O2_LABEL = "OxygenSaturation";
    private static final double BP_THRESHOLD = 90.0;
    private static final double O2_THRESHOLD = 92.0;

    @Override
    public boolean check(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());

        PatientRecord latestBP = null;
        PatientRecord latestO2 = null;

        for (int i = 0; i < records.size(); i++) {
            PatientRecord r = records.get(i);

            if (r.getRecordType().equals(BP_LABEL)) {
                if (latestBP == null || r.getTimestamp() > latestBP.getTimestamp()) {
                    latestBP = r;
                }
            }

            if (r.getRecordType().equals(O2_LABEL)) {
                if (latestO2 == null || r.getTimestamp() > latestO2.getTimestamp()) {
                    latestO2 = r;
                }
            }
        }

        if (latestBP == null || latestO2 == null) return false;

        boolean lowBP = latestBP.getMeasurementValue() <= BP_THRESHOLD;
        boolean lowO2 = latestO2.getMeasurementValue() <= O2_THRESHOLD;

        return lowBP && lowO2;
    }

    @Override
    public String getConditionName() {
        return "Hypotensive hypoxemia (BP ≤ 90 & O2 ≤ 92)";
    }
}
