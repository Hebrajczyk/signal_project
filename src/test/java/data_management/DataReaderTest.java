package data_management;

import com.data_management.DataStorage;

import com.data_management.PatientRecord;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataReaderTest {

    @Disabled
    public void testFakeDataReaderAddsData() throws Exception {
        DataStorage storage = new DataStorage();
        FakeDataReader reader = new FakeDataReader();

        reader.readData(storage);

        List<PatientRecord> records = storage.getRecords(1, 0, System.currentTimeMillis());

        assertEquals(2, records.size());
        assertEquals("HeartRate", records.get(0).getRecordType());
    }
}
