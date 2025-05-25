package com.data_management;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.handshake.ServerHandshake;

/**
 * WebSocket client handler that receives real time data and stores it
 */
public class WebSocketCMethods extends org.java_websocket.client.WebSocketClient {

    private final DataStorage storage;

    public WebSocketCMethods(String serverUri, DataStorage storage) throws URISyntaxException {
        super(new URI(serverUri));
        this.storage = storage;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to server");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received: " + message);


        String[] parts = message.split("\\|");
        if (parts.length != 4) {
            System.err.println("Invalid message format: " + message);
            return;
        }

        try {
            int patientId = Integer.parseInt(parts[0]);
            String type = parts[1];
            double value = Double.parseDouble(parts[2]);
            long timestamp = Long.parseLong(parts[3]);

            storage.addPatientData(patientId, value, type, timestamp);

        } catch (NumberFormatException e) {
            System.err.println("Error parsing message: " + e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("Error: " + ex.getMessage());
    }
}
