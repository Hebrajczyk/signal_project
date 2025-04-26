package alerts;

import com.alerts.*;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlertRulesTests {

    @Test
    public void testThresholdRuleAboveTrue() {
        Patient patient = new Patient(1);
        patient.addRecord(150.0, "HeartRate", System.currentTimeMillis());

        ThresholdRule rule = new ThresholdRule("HeartRate", 130.0, true);
        assertTrue(rule.check(patient), "Should trigger: value is above threshold");
    }

    @Test
    public void testThresholdRuleBelowFalse() {
        Patient patient = new Patient(1);
        patient.addRecord(105.0, "HeartRate", System.currentTimeMillis());

        ThresholdRule rule = new ThresholdRule("HeartRate", 100.0, false);
        assertFalse(rule.check(patient), "Should NOT trigger: value is above threshold");
    }

    @Test
    public void testBloodPressureTrendPositive() {
        Patient patient = new Patient(1);
        long baseTime = System.currentTimeMillis() - 15000; // 15 sekund temu

        patient.addRecord(100.0, "SystolicBloodPressure", baseTime);
        patient.addRecord(111.0, "SystolicBloodPressure", baseTime + 5000);
        patient.addRecord(123.0, "SystolicBloodPressure", baseTime + 10000);

        AlertRule rule = new SystolicBloodPressureRule();

        System.out.println("=== TEST: Trend Positive ===");
        for (var rec : patient.getRecords(0, System.currentTimeMillis())) {
            System.out.println("Record: " + rec.getRecordType() + ", " + rec.getMeasurementValue() + ", " + rec.getTimestamp());
        }

        assertTrue(rule.check(patient), "Should trigger: increasing trend by ≥ 10 mmHg");
    }



    @Test
    public void testBloodPressureTrendNegative() {
        Patient patient = new Patient(1);
        long baseTime = System.currentTimeMillis() - 15000;

        patient.addRecord(140.0, "SystolicBloodPressure", baseTime);
        patient.addRecord(128.0, "SystolicBloodPressure", baseTime + 5000);
        patient.addRecord(115.0, "SystolicBloodPressure", baseTime + 10000);

        AlertRule rule = new SystolicBloodPressureRule();

        // Logi do debugowania
        System.out.println("=== TEST: Trend Negative ===");
        for (var rec : patient.getRecords(0, System.currentTimeMillis())) {
            System.out.println("Record: " + rec.getRecordType() + ", " + rec.getMeasurementValue() + ", " + rec.getTimestamp());
        }

        assertTrue(rule.check(patient), "Should trigger: decreasing trend by ≥ 10 mmHg");
    }



    @Test
    public void testSaturationDropRuleTriggers() {
        Patient patient = new Patient(1);
        long now = System.currentTimeMillis();
        patient.addRecord(99.0, "OxygenSaturation", now - 5 * 60 * 1000); // 5 min ago
        patient.addRecord(92.0, "OxygenSaturation", now);

        AlertRule rule = new SaturationDropRule();
        assertTrue(rule.check(patient), "Should trigger: drop ≥ 5% in 10 min");
    }

    @Test
    public void testHypotensiveHypoxemiaRuleTriggers() {
        Patient patient = new Patient(1);
        long now = System.currentTimeMillis();
        patient.addRecord(88.0, "SystolicBloodPressure", now);
        patient.addRecord(90.0, "OxygenSaturation", now);

        AlertRule rule = new HypotensiveHypoxemiaRule();
        assertTrue(rule.check(patient), "Should trigger: BP ≤ 90 and O2 ≤ 92");
    }

    @Test
    public void testECGPeakRuleTriggers() {
        Patient patient = new Patient(1);
        long now = System.currentTimeMillis();
        patient.addRecord(0.5, "ECG", now - 3000);
        patient.addRecord(0.6, "ECG", now - 2000);
        patient.addRecord(1.5, "ECG", now);

        AlertRule rule = new ECGPeakRule();
        assertTrue(rule.check(patient), "Should trigger: latest ECG > average of previous");
    }

    @Test
    public void testTriggeredAlertRuleMock() {
        Patient patient = new Patient(1);
        long now = System.currentTimeMillis();

        patient.addRecord(123.0, "Alert", now);

        AlertRule rule = new AlertRule() {
            @Override
            public boolean check(Patient p) {
                return Math.abs(p.getRecords(0, System.currentTimeMillis())
                        .stream()
                        .filter(r -> r.getRecordType().equals("Alert"))
                        .mapToDouble(r -> r.getMeasurementValue())
                        .max().orElse(0) - 123.0) < 0.0001;
            }

            @Override
            public String getConditionName() {
                return "Mock manual alert with double value";
            }
        };

        assertTrue(rule.check(patient), "Should trigger: simulated manual alert value 123.0");
    }
}
