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
        storage.clearData();
        client = new WebSocketClientHandler("ws://localhost:8080", storage);
    }

    @Test
    public void testValidMessageParsesAndStoresCorrectly() {
        String validMessage = "1|HeartRate|88.5|" + System.currentTimeMillis();
        client.onMessage(validMessage);

        List<PatientRecord> records = storage.getRecords(1, 0, System.currentTimeMillis());
        assertFalse(records.isEmpty(), "Expected records for patient 1");

        PatientRecord record = records.get(records.size() - 1);
        assertEquals(1, record.getPatientId());
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(88.5, record.getMeasurementValue(), 0.001);
    }

    @Test
    public void testInvalidFormatIsHandledGracefully() {
        String invalidMessage = "1|HeartRate|INVALID MESSAGE|TIME";
        client.onMessage(invalidMessage);

        List<PatientRecord> records = storage.getRecords(1, 0, System.currentTimeMillis());
        assertEquals(0, records.size(), "No record should be stored for invalid message");
    }

    @Test
    public void testIncorrectPartsCount() {
        String badParts = "1|Only|Three";
        client.onMessage(badParts);

        List<PatientRecord> records = storage.getRecords(1, 0, System.currentTimeMillis());
        assertEquals(0, records.size(), "No record should be stored for invalid part count");
    }
}
