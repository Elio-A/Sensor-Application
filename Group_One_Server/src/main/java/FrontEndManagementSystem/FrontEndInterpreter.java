package FrontEndManagementSystem;

import Marshalling.InternalServerMarshall;
import SharedDataTypes.*;
import SharedDataTypes.SensorObjects.SimpleSensorObject;
import SharedDataTypes.SensorObjects.SimpleVirtualSensorObject;
import SharedDataTypes.SensorObjects.VirtualSensorObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.ZonedDateTime;

public class FrontEndInterpreter implements IFrontEndInterpreter {
    @Override
    public VirtualSensorObject getCurrentData(String sensorName){
        return InternalServerMarshall.getDataManagementSystem().RequestSensorData(sensorName);
    }

    @Override
    public VirtualSensorObject getSemiHistoric(String sensorName, String start){
        return InternalServerMarshall.getDataManagementSystem().RequestSensorData(sensorName, start);
    }

    @Override
    public VirtualSensorObject getFullHistoric(String sensorName, String start, String end){
        return InternalServerMarshall.getDataManagementSystem().RequestSensorData(sensorName, start, end);
    }

    @Override
    public String verifyLoginCredentials(String username, String password) {
        return InternalServerMarshall.getDataManagementSystem().getUserRole(username, password);
    }

    @Override
    public VirtualSensorObject[] getSensorList(String username) {
        return InternalServerMarshall.getDataManagementSystem().RequestSensorList(username);
    }

    @Override
    public TicketObject[] getTicketList(String userID) {
        return InternalServerMarshall.getDataManagementSystem().GetTickets(userID);
    }

    @Override
    public void UpdateTicket(String ticketID, String ticketState) {
        int value = 0;
        switch (ticketState) {
            case "NotStarted":
                value = 1;
                break;
            case "InProgress":
                value = 2;
                break;
            case "Completed":
                value = 3;
                break;
        }

        InternalServerMarshall.getDataManagementSystem().updateTicket(Integer.parseInt(ticketID), value);
    }

    @Override
    public void SetSchedule(String userID, String schedule) {
        String[] scheduleArr = schedule.split(":");
        InternalServerMarshall.getDataManagementSystem().Update_Schedule(userID, scheduleArr);
    }

    @Override
    public String GetSchedule(String username) {
        return InternalServerMarshall.getDataManagementSystem().Get_Schedule(username);
    }

    @Override
    public void createSensorAggregate(String sensorId,String nickName, String max, String min, String sampleRate, String equation, String type){
        VirtualSensorObject virtual = new SimpleVirtualSensorObject();
        virtual.name = sensorId;
        virtual.nickName = nickName;
        virtual.max= -1;
        virtual.min=-2;
        virtual.sampleRate= -1;
        virtual.location = "Sensor is virtual: no location.";
        virtual.description = equation;
        virtual.type= SensorType.Aggregated;
        virtual.state = SensorState.ActiveFunctioning;
        InternalServerMarshall.getDataManagementSystem().recordAggregateSensorData(virtual);
    }

    @Override
    public UserObject[] getAllUsers() {
        return InternalServerMarshall.getDataManagementSystem().Get_Researchers();
    }

    @Override
    public UserObject[] getAssignedUsers(String sensorID) {
        return InternalServerMarshall.getDataManagementSystem().Get_Researchers(sensorID);
    }

    @Override
    public void assignUsersToSensor(String sensorId, String users) {
        String[] usersA = users.split(":");
        InternalServerMarshall.getDataManagementSystem().Remove_sensor_access(usersA, sensorId);
    }

    @Override
    public void deleteTicket(int ticketID) {
        InternalServerMarshall.getDataManagementSystem().delete_ticket(ticketID);
    }

    @Override
    public TicketObject[] adminGetTicketList() {
        return InternalServerMarshall.getDataManagementSystem().get_all_tickets();
    }

    @Override
    public void changeSensorMetaData(String jsonVirtualSensorObject) {
        ObjectMapper objectMapper = new ObjectMapper(); // Initializing the ObjectMapper
        objectMapper.registerModule(new JavaTimeModule());

        SimpleVirtualSensorObject received = null;

        try {
            received = objectMapper.readValue(jsonVirtualSensorObject, SimpleVirtualSensorObject.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return; // Return from the method in case of JSON parsing exception
        }

        SimpleSensorObject toSend = new SimpleSensorObject();
        toSend.name = received.name;
        toSend.max = received.max;
        toSend.min = received.min;
        toSend.sampleRate = received.sampleRate;
        toSend.nickName = received.nickName;
        toSend.location = received.location;
        toSend.description = received.description;
        toSend.state = received.state;
        toSend.type = received.type;

        InternalServerMarshall.getSensorSystem().SendCommandToSensor(toSend);
    }


    @Override
    public String getTicketStatus(String sensorID) {
        return InternalServerMarshall.getDataManagementSystem().GetTicketState(sensorID);
    }
}
