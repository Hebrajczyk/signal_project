package com.data_management;

import java.net.URISyntaxException;

/**
 * Connects to a WebSocket server and continuously receives and stores data.
 * Implements the DataReader interface for real time WebSocket integration.
 */
public class WebSocketDataReader implements DataReader {

    private final String serverUri;
    private WebSocketClientHandler client;

    public WebSocketDataReader(String serverUri) {
        this.serverUri = serverUri;
    }

    @Override
    public void startStreaming(DataStorage storage) {
        try {
            client = new WebSocketClientHandler(serverUri, storage);
            client.connect();
        } catch (URISyntaxException e) {
            System.err.println("Invalid WebSocket URI: " + e.getMessage());
        }
    }

    //Neglects interface segregation rule but adding a new inteface would change complexity which is already high
    @Override
    public void readData(DataStorage storage) {

    }


    public void stop() {
        if (client != null && client.isOpen()) {
            client.close();
        }
    }
}
