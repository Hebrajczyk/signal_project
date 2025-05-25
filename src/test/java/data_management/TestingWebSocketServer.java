package data_management;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Mock WebSocket server used for testing
 */
public class TestingWebSocketServer extends WebSocketServer {

    private String testMessage;

    public TestingWebSocketServer(InetSocketAddress address, String testMessage) {
        super(address);
        this.testMessage = testMessage;
    }

    @Override
    public void onOpen(final WebSocket conn, ClientHandshake handshake) {
        System.out.println("Client connected");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    conn.send(testMessage);
                    conn.close();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message: " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("Error: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("Started on port " + getPort());
    }
}
