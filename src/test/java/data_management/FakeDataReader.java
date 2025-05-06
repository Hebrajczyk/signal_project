package data_management;

import com.data_management.DataStorage;

import java.io.IOException;

class FakeDataReader implements com.data_management.DataReader {

    @Override
    public void readData(DataStorage storage){
        long now = System.currentTimeMillis();
        storage.addPatientData(1, 110.0, "SystolicBloodPressure", now);
        storage.addPatientData(1, 120.0, "SystolicBloodPressure", now + 1000);
        storage.addPatientData(1, 130.0, "SystolicBloodPressure", now + 2000);
        storage.addPatientData(1, "triggered".hashCode(), "Alert", now + 3000);
    }


    @Override
    public void startStreaming(DataStorage storage) {

    }

}


