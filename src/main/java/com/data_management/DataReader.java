package com.data_management;

public interface DataReader {
    void startStreaming(DataStorage storage);
    void readData(DataStorage storage);
}
