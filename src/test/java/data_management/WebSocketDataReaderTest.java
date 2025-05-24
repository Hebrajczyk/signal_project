package data_management;

import com.data_management.DataStorage;
import com.data_management.WebSocketDataReader;
import org.junit.jupiter.api.*;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

public class WebSocketDataReaderTest {

    private static final int PORT = 8090;
    private static WebSocketServer mockServer;
    private DataStorage storage;

    @BeforeAll
    public static void startMockServer() {
        mockServer = new WebSocketServer(new InetSocketAddress(PORT)) {
            @Override
            public void onOpen(final WebSocket conn, final ClientHandshake handshake) {
                System.out.println("[Server] Client connected");

                Thread sender = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            conn.send("10|HeartRate|99.9|1234567890000");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                sender.start();
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                System.out.println("[Server] Connection closed");
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                // No action needed for this test
            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                System.err.println("[Server] Error: " + ex.getMessage());
            }

            @Override
            public void onStart() {
                System.out.println("[Server] Mock server started on port " + PORT);
            }
        };
        mockServer.start();
    }

    @AfterAll
    public static void stopMockServer() throws Exception {
        mockServer.stop();
    }

    @BeforeEach
    public void setUp() {
        storage = DataStorage.getInstance();
        storage.clearData();
    }

    @Test
    public void testWebSocketDataReaderIntegration() throws InterruptedException {
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:" + PORT);
        reader.startStreaming(storage);

        Thread.sleep(2000);

        assertEquals(1, storage.getAllPatients().size(), "Should have one patient stored");
        assertEquals(1, storage.getRecords(10, 0, Long.MAX_VALUE).size(), "Should have one record stored");

        double value = storage.getRecords(10, 0, Long.MAX_VALUE).get(0).getMeasurementValue();
        assertEquals(99.9, value, 0.01, "Heart rate should match expected value");
    }
}
