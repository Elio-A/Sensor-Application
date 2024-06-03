package MockSystem;

import Communications.CommunicationsController;
import Marshalling.InternalServerMarshall;
import SensorManagementSystem.ISensorManagementSystem;
import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorObjects.SimpleSensorObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Mock_SensorManager implements ISensorManagementSystem {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean InitializeSystem() {

        objectMapper.registerModule(new JavaTimeModule());

        return true;
    }
    public boolean SendCommandToSensor(SensorObject newState){
        System.out.println(newState.toString());
        return true;
    }

    @Override
    public void onMessageReceived(String message, String sessionID) {
        SimpleSensorObject sensorObject = null;
        try {
            sensorObject = objectMapper.readValue(message, SimpleSensorObject.class);
        } catch (Exception e) {
            System.out.println("ERROR : " + e.getMessage());
        }
        InternalServerMarshall.getDataManagementSystem().recordSensorData(sensorObject);
    }

}
