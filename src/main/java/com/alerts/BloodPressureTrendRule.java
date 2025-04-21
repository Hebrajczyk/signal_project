package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Triggers an alert if there is a rising or falling trend in systolic blood pressure
 * across 3 consecutive measurements with at least 10 mmHg difference between each.
 */
public class BloodPressureTrendRule implements AlertRule {

    private static final String LABEL = "SystolicBloodPressure";
    private static final double MIN_STEP = 10.0;

    @Override
    public boolean check(Patient patient) {
        List<PatientRecord> allRecords = patient.getRecords(0, System.currentTimeMillis());
        List<PatientRecord> bpRecords = new ArrayList<>();

        for (int i = 0; i < allRecords.size(); i++) {
            PatientRecord r = allRecords.get(i);
            if (r.getRecordType().equals(LABEL)) {
                bpRecords.add(r);
            }
        }


        bpRecords.sort(Comparator.comparingLong(PatientRecord::getTimestamp));

        if (bpRecords.size() < 3) return false;


        for (int i = 0; i <= bpRecords.size() - 3; i++) {
            double v1 = bpRecords.get(i).getMeasurementValue();
            double v2 = bpRecords.get(i + 1).getMeasurementValue();
            double v3 = bpRecords.get(i + 2).getMeasurementValue();

            boolean increasing = (v2 - v1 >= MIN_STEP) && (v3 - v2 >= MIN_STEP);
            boolean decreasing = (v1 - v2 >= MIN_STEP) && (v2 - v3 >= MIN_STEP);

            if (increasing || decreasing) return true;
        }

        return false;
    }

    @Override
    public String getConditionName() {
        return "Blood pressure trend (↑ or ↓, step ≥ 10)";
    }
}
