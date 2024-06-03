package FrontEndManagementSystem;

import SharedDataTypes.SensorObjects.VirtualSensorObject;
import SharedDataTypes.TicketObject;
import SharedDataTypes.UserObject;

import java.time.ZonedDateTime;

public interface IFrontEndInterpreter {
    public VirtualSensorObject getCurrentData(String sensorName);

    public VirtualSensorObject getSemiHistoric(String sensorName, String start);

    public VirtualSensorObject getFullHistoric(String sensorName, String start, String end);
    public String verifyLoginCredentials(String username, String password);
    public VirtualSensorObject[] getSensorList(String username);
    public TicketObject[] getTicketList(String userID);
    public void UpdateTicket(String ticketID, String ticketState);
    public void SetSchedule(String userID, String schedule);
    public String GetSchedule(String username);
    public void createSensorAggregate(String sensorId,String nickName, String max, String min, String sampleRate, String equation, String type);
    public UserObject[] getAllUsers();
    public UserObject[] getAssignedUsers(String sensorID);
    public void assignUsersToSensor(String sensorId, String users);
    public void deleteTicket(int ticketID);
    public TicketObject[] adminGetTicketList();
    public void changeSensorMetaData(String jsonVirtualSensorObject);
    public String getTicketStatus(String sensorID);

}
