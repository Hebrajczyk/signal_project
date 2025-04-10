package data_management;

import com.data_management.DataStorage;

import java.io.IOException;

class FakeDataReader implements com.data_management.DataReader {

    public void read(DataStorage storage) {
        // zostaw puste jeśli niepotrzebne w tym teście
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {

    }
}


