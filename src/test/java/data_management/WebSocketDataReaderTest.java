package data_management;

import com.data_management.DataStorage;
import com.data_management.WebSocketDataReader;
import org.junit.jupiter.api.*;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

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
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                System.out.println("[Server] Client connected");

                conn.send("10|HeartRate|99.9|1234567890000");
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                System.out.println("[Server] Connection closed");
            }

            @Override
            public void onMessage(WebSocket conn, String message) {

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


        Thread.sleep(1000);

        assertEquals(1, storage.getAllPatients().size());
        assertEquals(1, storage.getRecords(10, 0, Long.MAX_VALUE).size());
        assertEquals(99.9, storage.getRecords(10, 0, Long.MAX_VALUE).get(0).getMeasurementValue());
    }
}
