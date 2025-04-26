package com.data_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alerts.AlertGenerator;

/**
 * Manages storage and retrieval of patient data within a healthcare monitoring
 * system.
 * This class serves as a repository for all patient records, organized by
 * patient IDs.
 */
public class DataStorage {
    private Map<Integer, Patient> patientMap;

    /**
     * Constructs a new instance of DataStorage, initializing the underlying storage
     * structure.
     */
    public DataStorage() {
        this.patientMap = new HashMap<>();
    }

    /**
     * Adds or updates patient data in the storage.
     *
     * @param patientId the unique identifier of the patient
     * @param measurementValue the value of the health metric being recorded
     * @param recordType the type of record, e.g., "HeartRate", "BloodPressure"
     * @param timestamp the time at which the measurement was taken
     */
    public void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
        Patient patient = patientMap.get(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        }
        patient.addRecord(measurementValue, recordType, timestamp);
    }

    /**
     * Retrieves a list of PatientRecord objects for a specific patient, filtered by a time range.
     *
     * @param patientId the unique identifier of the patient
     * @param startTime the start of the time range
     * @param endTime the end of the time range
     * @return a list of PatientRecord objects that fall within the specified time range
     */
    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        } else {
            return new ArrayList<PatientRecord>();
        }
    }

    /**
     * Retrieves a collection of all patients stored in the data storage.
     *
     * @return a list of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<Patient>(patientMap.values());
    }

    /**
     * Retrieves a patient by their ID.
     *
     * @param patientId the ID of the patient
     * @return the Patient object or null if not found
     */
    public Patient getPatientById(int patientId) {
        return patientMap.get(patientId);
    }

    /**
     * The main method for the DataStorage class.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        DataStorage storage = new DataStorage();

        List<PatientRecord> records = storage.getRecords(1, 1700000000000L, 1800000000000L);
        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);
            System.out.println("Record for Patient ID: " + record.getPatientId() +
                    ", Type: " + record.getRecordType() +
                    ", Data: " + record.getMeasurementValue() +
                    ", Timestamp: " + record.getTimestamp());
        }

        AlertGenerator alertGenerator = new AlertGenerator(storage);

        List<Patient> patients = storage.getAllPatients();
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            alertGenerator.evaluateData(patient);
        }
    }
}
