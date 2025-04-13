
Data Storage System – Explanation

1 Purpose
The system is responsible for efficiently storing, retrieving, and managing patient data records. It must support time-based queries, label-based filtering(based on week 2 ).

2 Core Components

2.1 Existing Components (Already Implemented)

PatientRecord: Represents a single data point for a patient.

Patient: Holds a patient ID and a list of their PatientRecords. Allows filtering of records by time.

DataStorage: Manages a map of patients. Allows adding new data and retrieving patient records.

DataReader (interface only): Provides a method for loading data from an external source.

2.2 Components to be added

RetentionPolicy: Needed for data cleanup requirement described in Week 2. Responsible for removing outdated records from each patient.

DataRetriever: Designed to support complex data queries, such as filtering by time range or record type. 

3 How the System Works

DataReader reads data and populates DataStorage.

DataStorage holds patients and their records, and allows access by time or ID.

Patient objects store lists of PatientRecords and allow filtering by time range.

RetentionPolicy is triggered to remove records older than a specified threshold.

DataRetriever allows external systems or users to query data in flexible ways.
