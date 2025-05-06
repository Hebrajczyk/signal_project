package data_management;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.WebSocketClientHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WebSocketClientHandlerTest {

    private WebSocketClientHandler client;
    private DataStorage storage;

    @BeforeEach
    public void setup() throws URISyntaxException {
        storage = DataStorage.getInstance();
        client = new WebSocketClientHandler("ws://localhost:8080", storage);
    }

    @Test
    public void testValidMessageParsesAndStoresCorrectly() {
        // If
        String validMessage = "1|HeartRate|88.5|" + System.currentTimeMillis();

        // When
        client.onMessage(validMessage);

        // Then
        List<PatientRecord> records = storage.getRecords(1, 0, System.currentTimeMillis());
        assertFalse(records.isEmpty(), "Expected records for patient 1");
        PatientRecord record = records.get(records.size() - 1);

        assertEquals(1, record.getPatientId());
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(88.5, record.getMeasurementValue(), 0.001);
    }

    @Test
    public void testInvalidFormatIsHandledGracefully() {
        // If
        String invalidMessage = "1|HeartRate|BAD_NUMBER|TIME";

        // When
        client.onMessage(invalidMessage);

        // Then
        List<PatientRecord> records = storage.getRecords(1, 0, System.currentTimeMillis());
        assertTrue(records.isEmpty(), "No record should be stored for invalid message");
    }

    @Test
    public void testIncorrectPartsCount() {
        // If
        String badParts = "1|Only|Three";

        // When
        client.onMessage(badParts);

        // Then
        List<PatientRecord> records = storage.getRecords(1, 0, System.currentTimeMillis());
        assertTrue(records.isEmpty(), "No record should be stored for invalid part count");
    }
}
