package alerts;

import com.alerts.*;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlertRulesTests {

    @Test
    public void testThresholdRuleAboveTrue() {
        Patient patient = new Patient(1);
        patient.addRecord(150.0, "HeartRate", System.currentTimeMillis());

        AlertRule rule = new ThresholdRule(new ThresholdStrategy("HeartRate", 130.0, true));
        assertTrue(rule.check(patient), "Should trigger: value is above threshold");
    }

    @Test
    public void testThresholdRuleBelowFalse() {
        Patient patient = new Patient(1);
        patient.addRecord(105.0, "HeartRate", System.currentTimeMillis());

        AlertRule rule = new ThresholdRule(new ThresholdStrategy("HeartRate", 100.0, false));
        assertFalse(rule.check(patient), "Should NOT trigger: value is above threshold");
    }

    @Test
    public void testBloodPressureTrendPositive() {
        Patient patient = new Patient(1);
        long baseTime = System.currentTimeMillis() - 15000;

        patient.addRecord(100.0, "SystolicBloodPressure", baseTime);
        patient.addRecord(111.0, "SystolicBloodPressure", baseTime + 5000);
        patient.addRecord(123.0, "SystolicBloodPressure", baseTime + 10000);

        AlertRule rule = new SystolicBloodPressureRule(new SystolicBloodPressureStrategy());

        System.out.println("=== TEST: Trend Positive ===");
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());
        for (int i = 0; i < records.size(); i++) {
            PatientRecord rec = records.get(i);
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

        AlertRule rule = new SystolicBloodPressureRule(new SystolicBloodPressureStrategy());

        System.out.println("=== TEST: Trend Negative ===");
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());
        for (int i = 0; i < records.size(); i++) {
            PatientRecord rec = records.get(i);
            System.out.println("Record: " + rec.getRecordType() + ", " + rec.getMeasurementValue() + ", " + rec.getTimestamp());
        }

        assertTrue(rule.check(patient), "Should trigger: decreasing trend by ≥ 10 mmHg");
    }

    @Test
    public void testSaturationDropRuleTriggers() {
        Patient patient = new Patient(1);
        long now = System.currentTimeMillis();
        patient.addRecord(99.0, "OxygenSaturation", now - 5 * 60 * 1000);
        patient.addRecord(92.0, "OxygenSaturation", now);

        AlertRule rule = new SaturationDropRule(new SaturationDropStrategy());
        assertTrue(rule.check(patient), "Should trigger: drop ≥ 5% in 10 min");
    }

    @Test
    public void testHypotensiveHypoxemiaRuleTriggers() {
        Patient patient = new Patient(1);
        long now = System.currentTimeMillis();
        patient.addRecord(88.0, "SystolicBloodPressure", now);
        patient.addRecord(90.0, "OxygenSaturation", now);

        AlertRule rule = new HypotensiveHypoxemiaRule(new HypotensiveHypoxemiaStrategy());
        assertTrue(rule.check(patient), "Should trigger: BP ≤ 90 and O2 ≤ 92");
    }

    @Test
    public void testECGPeakRuleTriggers() {
        Patient patient = new Patient(1);
        long now = System.currentTimeMillis();
        patient.addRecord(0.5, "ECG", now - 3000);
        patient.addRecord(0.6, "ECG", now - 2000);
        patient.addRecord(1.5, "ECG", now);

        AlertRule rule = new ECGPeakRule(new ECGPeakStrategy());
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
                List<PatientRecord> records = p.getRecords(0, System.currentTimeMillis());
                double maxValue = 0;
                for (int i = 0; i < records.size(); i++) {
                    PatientRecord r = records.get(i);
                    if (r.getRecordType().equals("Alert")) {
                        if (r.getMeasurementValue() > maxValue) {
                            maxValue = r.getMeasurementValue();
                        }
                    }
                }
                return Math.abs(maxValue - 123.0) < 0.0001;
            }

            @Override
            public String getConditionName() {
                return "Mock manual alert with double value";
            }
        };

        assertTrue(rule.check(patient), "Should trigger: simulated manual alert value 123.0");
    }

    @Test
    public void testBloodPressureAlertFactory() {
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("1", "High systolic pressure", 1234567890L);

        assertEquals("1", alert.getPatientId());
        assertEquals("Blood Pressure Alert: High systolic pressure", alert.getCondition());
        assertEquals(1234567890L, alert.getTimestamp());
    }

    @Test
    public void testBloodOxygenAlertFactory() {
        AlertFactory factory = new BloodOxygenAlertFactory();
        Alert alert = factory.createAlert("2", "Oxygen drop detected", 9876543210L);

        assertEquals("2", alert.getPatientId());
        assertEquals("Blood Oxygen Alert: Oxygen drop detected", alert.getCondition());
        assertEquals(9876543210L, alert.getTimestamp());
    }

    @Test
    public void testECGAlertFactory() {
        AlertFactory factory = new ECGAlertFactory();
        Alert alert = factory.createAlert("3", "ECG peak detected", 1122334455L);

        assertEquals("3", alert.getPatientId());
        assertEquals("ECG Alert: ECG peak detected", alert.getCondition());
        assertEquals(1122334455L, alert.getTimestamp());
    }

    @Test
    public void testPriorityAlertDecorator() {
        AlertInterface alert = new Alert("5", "Heart Rate Critical", System.currentTimeMillis());
        AlertInterface decoratedAlert = new PriorityAlertDecorator(alert, "High");

        assertTrue(decoratedAlert.getCondition().contains("[Priority: High]"));
        assertEquals(alert.getPatientId(), decoratedAlert.getPatientId());
        assertEquals(alert.getTimestamp(), decoratedAlert.getTimestamp());
    }

    @Test
    public void testRepeatedAlertDecorator() {
        AlertInterface alert = new Alert("6", "Blood Pressure Drop", System.currentTimeMillis());
        AlertInterface decoratedAlert = new RepeatedAlertDecorator(alert);

        assertTrue(decoratedAlert.getCondition().contains("[Repeated]"));
        assertEquals(alert.getPatientId(), decoratedAlert.getPatientId());
        assertEquals(alert.getTimestamp(), decoratedAlert.getTimestamp());
    }

}
