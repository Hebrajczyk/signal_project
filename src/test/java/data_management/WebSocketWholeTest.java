package data_management;


import com.alerts.*;
import com.data_management.*;

import org.junit.jupiter.api.Test;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

public class WebSocketWholeTest {

    @Test
    public void testAlertTriggeredFromWebSocketMessage() throws Exception {
        int port = 8091;
        DataStorage storage = DataStorage.getInstance();
        storage.clearData();


        AlertGenerator generator = new AlertGenerator(storage, new BloodPressureAlertFactory());
        generator.addRule(1, new ThresholdRule(new AlertStrategy() {
            @Override
            public boolean checkAlert(Patient patient) {
                return false;
            }

            public boolean check(Patient patient) {
                return patient.getRecords(0, System.currentTimeMillis()).stream()
                        .anyMatch(r -> r.getRecordType().equals("HeartRate") && r.getMeasurementValue() > 130);
            }
            public String getConditionName() {
                return "High Heart Rate";
            }
        }));


        AlertManager mockAlertManager = new AlertManager() {
            @Override
            public void dispatchAlert(Alert alert) {
                System.out.println("✅ ALERT TRIGGERED: " + alert.getCondition());
                assertEquals("High Heart Rate", alert.getCondition());
                assertEquals("1", alert.getPatientId());
            }
        };


        generator.setAlertManager(mockAlertManager);


        WebSocketServer server = new TestingWebSocketServer(new InetSocketAddress(port),
                "1|HeartRate|140.0|" + System.currentTimeMillis());
        server.start();


        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:" + port);
        reader.startStreaming(storage);

        Thread.sleep(3000);


        Patient patient = storage.getPatientById(1);
        assertNotNull(patient);
        assertFalse(patient.getRecords(0, System.currentTimeMillis()).isEmpty());

        server.stop();
    }
}
