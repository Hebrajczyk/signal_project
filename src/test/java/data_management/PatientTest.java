package data_management;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PatientTest {

    @Test
    public void testGetRecordsInRange() {
        Patient patient = new Patient(1);

        long baseTime = System.currentTimeMillis();


        patient.addRecord(80.0, "HeartRate", baseTime - 100000);
        patient.addRecord(90.0, "HeartRate", baseTime + 1000);
        patient.addRecord(100.0, "HeartRate", baseTime + 2000);

        long start = baseTime;
        long end = baseTime + 3000;

        List<PatientRecord> results = patient.getRecords(start, end);

        assertEquals(2, results.size());
        assertEquals(90.0, results.get(0).getMeasurementValue());
        assertEquals(100.0, results.get(1).getMeasurementValue());
    }

    @Test
    public void testGetRecordsEmpty() {
        Patient patient = new Patient(1);
        List<PatientRecord> results = patient.getRecords(0, System.currentTimeMillis());
        assertTrue(results.isEmpty());
    }
}
