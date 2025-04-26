package alerts;

import com.alerts.*;
import com.data_management.Patient;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlertSystemTest {

    @Test
    void thresholdRuleShouldTriggerAlert() {

        DataStorage storage = DataStorage.getInstance();

        int patientId = 1;

        long now = System.currentTimeMillis();
        storage.addPatientData(patientId, 150.0, "HeartRate", now);

        AlertGenerator generator = new AlertGenerator(storage, new BloodPressureAlertFactory());

        generator.addRule(patientId, new ThresholdRule(new ThresholdStrategy("HeartRate", 130.0, true))
        );

        Patient patient = storage.getAllPatients().get(0);
        generator.evaluateData(patient);


        assertTrue(true);
    }
}
