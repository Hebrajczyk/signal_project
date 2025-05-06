package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * Triggers an alert if the most recent Alert record has value "triggered"
 */
public class TriggeredAlertRule implements AlertRule {

    private static final String LABEL = "Alert";
    private static final String TRIGGER = "triggered";

    @Override
    public boolean check(Patient patient) {
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

        if (latest == null) return false;

        String value = String.valueOf(latest.getMeasurementValue()).toLowerCase();

        return value.equals(TRIGGER);
    }

    @Override
    public String getConditionName() {
        return "Manual alert triggered";
    }
}
