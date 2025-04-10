package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A file-based implementation of OutputStrategy that writes
 * patient data to individual text files grouped by label.
 */

public class FileOutputStrategy implements OutputStrategy {

    /**
     * Constructs a FileOutputStrategy with a given base directory.
     *
     * @param baseDirectory the folder where files will be written
     */
    private String baseDirectory;
    private final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>();


    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }



    /**
     * Writes patient data to a file. The file is based on the label (HeartRate.txt).
     * If the file does not exist, it is created.
     *
     * @param patientId the patient's ID
     * @param timestamp time of measurement in milliseconds
     * @param label the type of measurement (HeartRate)
     * @param data the actual data (82.0)
     */

    @Override
    public void output(int patientId, long timestamp, String label, String data) {

        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }

        // Set the filePath variable
        String filePath = file_map.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());


        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }

    }
}