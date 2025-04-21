package alerts;

import com.alerts.*;
import com.data_management.Patient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlertRulesTests {

    @Test
    public void testThresholdRuleAboveTrue() {
        Patient patient = new Patient(1);
        patient.addRecord(150.0, "HeartRate", System.currentTimeMillis());

        ThresholdRule rule = new ThresholdRule("HeartRate", 130.0, true);
        assertTrue(rule.check(patient));
    }

    @Test
    public void testThresholdRuleBelowFalse() {
        Patient patient = new Patient(1);
        patient.addRecord(105.0, "HeartRate", System.currentTimeMillis());

        ThresholdRule rule = new ThresholdRule("HeartRate", 100.0, false);
        assertFalse(rule.check(patient));
    }


    @Disabled
    public void testBloodPressureTrendPositive() {
        Patient patient = new Patient(1);
        long now = System.currentTimeMillis();
        patient.addRecord(110.0, "SystolicBloodPressure", now);
        patient.addRecord(120.0, "SystolicBloodPressure", now + 1000);
        patient.addRecord(130.0, "SystolicBloodPressure", now + 2000);

        AlertRule rule = new BloodPressureTrendRule();
        assertTrue(rule.check(patient));
    }



    @Disabled
    public void testBloodPressureTrendNegative() {
        Patient patient = new Patient(1);
        long now = System.currentTimeMillis();
        patient.addRecord(130.0, "SystolicBloodPressure", now);
        patient.addRecord(120.0, "SystolicBloodPressure", now + 1000);
        patient.addRecord(110.0, "SystolicBloodPressure", now + 2000);

        AlertRule rule = new BloodPressureTrendRule();
        assertTrue(rule.check(patient));
    }





    @Test
    public void testSaturationDropRuleTriggers() {
        Patient patient = new Patient(1);
        long now = System.currentTimeMillis();
        patient.addRecord(99.0, "OxygenSaturation", now - 5 * 60 * 1000); // 5 min ago
        patient.addRecord(92.0, "OxygenSaturation", now);

        AlertRule rule = new SaturationDropRule();
        assertTrue(rule.check(patient));
    }

    @Test
    public void testHypotensiveHypoxemiaRuleTriggers() {
        Patient patient = new Patient(1);
        long now = System.currentTimeMillis();
        patient.addRecord(88.0, "SystolicBloodPressure", now);
        patient.addRecord(90.0, "OxygenSaturation", now);

        AlertRule rule = new HypotensiveHypoxemiaRule();
        assertTrue(rule.check(patient));
    }

    @Test
    public void testECGPeakRuleTriggers() {
        Patient patient = new Patient(1);
        long now = System.currentTimeMillis();
        patient.addRecord(0.5, "ECG", now - 3000);
        patient.addRecord(0.6, "ECG", now - 2000);
        patient.addRecord(1.5, "ECG", now);

        AlertRule rule = new ECGPeakRule();
        assertTrue(rule.check(patient));
    }

    @Disabled
    public void testTriggeredAlertRule() {
        Patient patient = new Patient(1);
        long now = System.currentTimeMillis();
        patient.addRecord("triggered".hashCode(), "Alert", now);

        AlertRule rule = new TriggeredAlertRule();
        assertTrue(rule.check(patient));
    }





}
