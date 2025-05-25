package com.data_management;

public interface DataReader {
    void streaming(DataStorage storage);
    void readData(DataStorage storage);
}
