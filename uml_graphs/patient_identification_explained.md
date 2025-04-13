 Patient Identification System – Explanation

1 Purpose

This system is responsible for correctly identifying patients when new data is received. It supports multiple matching mechanisms and handle mismatches or anomalies. 


2 Core Components

2.1 Existing Components (Already Implemented)

PatientRecord:  
Represents a single medical observation. Contains fields for `patientId`, `recordType`, `measurementValue`, and `timestamp`.

Patient:  
Stores a patient’s identity (`patientId`, `name`, `dateOfBirth`) and list of `PatientRecord`s. There is filtering records by time.

DataStorage:  
Has a map of all patients by ID. Provides basic retrieval methods and data access via patient ID.

2.2 Components to Be Added

PatientInputData:  
A new class for representing raw incoming data. Needed to pass name, dateOfBirth, and ID in one structure for matching and validation.

PatientIdentifier:  
Main controller of identification logic. Matches input data to stored patients using both ID and name and date of birth. If no match is found, it triggers mismatchHandler.

MismatchHandler:  
Handles anomalies like unmatched patients or inconsistent data. Notifies about mismatches and optionally reports serious issues.


3 Why Changes Were Needed(what I want to add)

- Identification must not rely only on `patientId`, which may be incorrect or missing.
- Name and date of birth should be considered for validation.
- The system must detect and respond to mismatches, not ignore them.
- Alerts are required when anomalies occur.

Bcs of it the addition of `PatientInputData`, `PatientIdentifier`, and `MismatchHandler`.


4 How the System will work

1. System provides raw patient input → stored in a `PatientInputData` object.

2. `PatientIdentifier` uses:
    - `DataStorage.getPatientById()` for fast lookup.
    - `DataStorage.findPatientByNameAndDOB()` as a checking meassure.

3. If no match is found → `MismatchHandler.logMismatch()` is called.

4. If a patient is matched → the system can continue to associate measurements.

5. `MismatchHandler` is responsible for logging mismatches and optionally reporting anomalies with `reportAnomaly()`.

6 Summary

This updated system improves reliability and traceability of patient data association.


- Supporting multiple matching strategies.
- Informing about errors on invalid input.
- Reusable classes for identification.
