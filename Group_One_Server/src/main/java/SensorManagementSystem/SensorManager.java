package SensorManagementSystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import Communications.CommunicationsController;
import Marshalling.InternalServerMarshall;
import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorObjects.SimpleSensorObject;

public class SensorManager implements ISensorManagementSystem{


    @Override
    public boolean InitializeSystem() {

        return true;
    }
    public boolean SendCommandToSensor(SensorObject newState){
        sendSensorData(newState);
        return true;
    }
    public void sendSensorData(SensorObject sensor) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // For handling Java Time objects

        try {
            String jsonSensor = objectMapper.writeValueAsString(sensor);
            CommunicationsController.Session session = InternalServerMarshall.getCommunicationsController().findSessionByName(sensor.name);
            if (session != null) {
                CommunicationsController.Command command = new CommunicationsController.Command(session.getName(), session.getPort(), jsonSensor);
                session.sendMessage(jsonSensor); // Assuming this method sends the command
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle the error appropriately
        }
    }
    @Override
    public void onMessageReceived(String message, String sessionID) {
        System.out.println("HERE: "+message);

        ObjectMapper objectMapper = new ObjectMapper(); //initialzing the ObjectMapper
        objectMapper.registerModule(new JavaTimeModule());
        
        SimpleSensorObject toSend = null;

        try {
            toSend = objectMapper.readValue(message, SimpleSensorObject.class);
        } 
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //InternalServerMarshall.getDataManagementSystem().recordSensorData(toSend);

        InternalServerMarshall.getMonitoringSystem().MonitorSensor(toSend);

    }
}
