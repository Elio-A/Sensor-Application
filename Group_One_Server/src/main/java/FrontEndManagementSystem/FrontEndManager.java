package FrontEndManagementSystem;


import Communications.CommunicationsController;
import Marshalling.InternalServerMarshall;
import SharedDataTypes.SensorObjects.VirtualSensorObject;
import SharedDataTypes.TicketObject;
import SharedDataTypes.UserObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.ZonedDateTime;
import java.util.concurrent.CompletableFuture;


public class FrontEndManager implements IFrontEndManagementSystem{
    private FrontEndInterpreter frontEndInterpreter;
    @Override
    public boolean InitializeSystem() {
        frontEndInterpreter = new FrontEndInterpreter();
        return true;
    }
    public void SendStateFromMonitor(VirtualSensorObject[] sensorsStates){

    }
    public void SendStateFromDMS(VirtualSensorObject[] sensorsStates){

    }

    @Override
    public CompletableFuture<String> onMessageRecieved(String message, String id) {
        String[] messageFormat = message.split(";");



        return CompletableFuture.supplyAsync(() -> {
            String jsonToSend = "ERROR : NULL RESPONSE";
            String json =  "ERROR : NULL RESPONSE";
            VirtualSensorObject sensorObject = null;
            VirtualSensorObject[] sensorObjects;
            TicketObject ticketObject = null;
            TicketObject[] ticketObjects;
            UserObject userObject = null;
            UserObject[] userObjects;
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

            try {
                for (int i = 0; i< messageFormat.length;i++){
                    System.out.println("!!!!!!!!!!!!!!!!!!! "+messageFormat[i]);
                }
                switch (messageFormat[0]) {
                    case "real time":
                        sensorObject = frontEndInterpreter.getCurrentData(messageFormat[1]);
                        json = objectMapper.writeValueAsString(sensorObject);
                        break;
                    case "semi historic":
                        sensorObject = frontEndInterpreter.getSemiHistoric(messageFormat[1], messageFormat[2]);
                        json = objectMapper.writeValueAsString(sensorObject);
                        break;
                    case "full historic":
                        sensorObject = frontEndInterpreter.getFullHistoric(messageFormat[1], messageFormat[2], messageFormat[3]);
                        json = objectMapper.writeValueAsString(sensorObject);
                        break;
                    case "sensor list":
                        sensorObjects = frontEndInterpreter.getSensorList(messageFormat[1]);
                        json = objectMapper.writeValueAsString(sensorObjects);
                        break;
                    case "ticket list":
                        ticketObjects = frontEndInterpreter.getTicketList(messageFormat[1]);
                        json = objectMapper.writeValueAsString(ticketObjects);
                        break;
                    case "verify login":
                        json = frontEndInterpreter.verifyLoginCredentials(messageFormat[1], messageFormat[2]);
                        break;
                    case "get schedule":
                        json = frontEndInterpreter.GetSchedule(messageFormat[1]);
                        break;
                    case "set schedule":
                        frontEndInterpreter.SetSchedule(messageFormat[1], messageFormat[2]);
                        json = "done";
                        break;
                    case "save ticket status":
                        frontEndInterpreter.UpdateTicket(messageFormat[1], messageFormat[2]);
                        json = "done";
                        break;
                    case "aggregate":
                        frontEndInterpreter.createSensorAggregate(messageFormat[1], messageFormat[2], messageFormat[3], messageFormat[4], messageFormat[5], messageFormat[6], messageFormat[7]);
                        break;
                    case "get all users":
                        userObjects =  frontEndInterpreter.getAllUsers();
                        json = objectMapper.writeValueAsString(userObjects);
                        break;
                    case "get assigned users":
                        userObjects = frontEndInterpreter.getAssignedUsers(messageFormat[1]);
                        json = objectMapper.writeValueAsString(userObjects);
                        break;
                    case "assign users":
                        frontEndInterpreter.assignUsersToSensor(messageFormat[1], messageFormat[2]);
                        json = "done";
                        break;
                    case "delete ticket":
                        frontEndInterpreter.deleteTicket(Integer.parseInt(messageFormat[1]));
                        json = "done";
                        break;
                    case "admin ticket list":
                        ticketObjects = frontEndInterpreter.adminGetTicketList();
                        json = objectMapper.writeValueAsString(ticketObjects);
                        break;
                    case "change sensor":
                        frontEndInterpreter.changeSensorMetaData(messageFormat[1]);
                        json = "done";
                        break;
                    case "get ticket status":
                        json = frontEndInterpreter.getTicketStatus(messageFormat[1]);
                        break;

                }
                System.out.println("JSON TO SEND: "+json);

                CommunicationsController.Command commandToSend = new CommunicationsController.Command("Server", "00", json);
                jsonToSend = objectMapper.writeValueAsString(commandToSend);

            } catch (Exception e) {
                System.out.println("FUCK-UP: "+e);

                // Handle the interruption
                Thread.currentThread().interrupt();
            }

            return jsonToSend;
        });
    }
}
