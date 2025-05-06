package com.data_management;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * WebSocket client handler that receives real-time data and stores it.
 */
public class WebSocketClientHandler extends WebSocketClient {

    private final DataStorage storage;

    public WebSocketClientHandler(String serverUri, DataStorage storage) throws URISyntaxException {
        super(new URI(serverUri));
        this.storage = storage;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("[Client] Connected to WebSocket server");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("[Client] Received: " + message);


        String[] parts = message.split("\\|");
        if (parts.length != 4) {
            System.err.println("[Client] Invalid message format: " + message);
            return;
        }

        try {
            int patientId = Integer.parseInt(parts[0]);
            String type = parts[1];
            double value = Double.parseDouble(parts[2]);
            long timestamp = Long.parseLong(parts[3]);

            storage.addPatientData(patientId, value, type, timestamp);

        } catch (NumberFormatException e) {
            System.err.println("[Client] Error parsing message: " + e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("[Client] Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("[Client] Error: " + ex.getMessage());
    }
}
