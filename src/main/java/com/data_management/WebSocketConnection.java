package com.data_management;

import java.net.URISyntaxException;

/**
 * Connects to a WebSocket server and continuously receives and stores data.
 * Implements the DataReader interface for real time WebSocket integration.
 */
public class WebSocketConnection implements DataReader {

    private final String serverUri;
    private WebSocketCMethods client;

    public WebSocketConnection(String serverUri) {
        this.serverUri = serverUri;
    }

    @Override
    public void streaming(DataStorage storage) {
        try {
            client = new WebSocketCMethods(serverUri, storage);
            client.connect();
        } catch (URISyntaxException e) {
            System.err.println("Wrong URI: " + e.getMessage());
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
