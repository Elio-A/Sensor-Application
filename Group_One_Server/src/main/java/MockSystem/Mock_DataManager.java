package MockSystem;

import DataManagementSystem.IDataManagementSystem;
import SharedDataTypes.*;
import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorObjects.SimpleVirtualSensorObject;
import SharedDataTypes.SensorObjects.VirtualSensorObject;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Random;

public class Mock_DataManager implements IDataManagementSystem {
    private Random random;
    private SensorObject currentData;


    @Override
    public boolean InitializeSystem() {
        random = new Random();

        return true;
    }

    @Override
    public VirtualSensorObject RequestSensorData(String sensorName, String start, String end) {
        VirtualSensorObject reply = new SimpleVirtualSensorObject();

        reply.name = sensorName;
        reply.samples = new SensorSample[] {
                new SensorSample() {{
                    sampleDateTime = start;
                    value = random.nextInt(50);
                }},
                new SensorSample() {{
                    sampleDateTime = end;
                    value = random.nextInt(50);
                }}
        };

        return reply;
    }

    @Override
    public VirtualSensorObject RequestSensorData(String sensorName, String start) {
        VirtualSensorObject reply = convertToVirtual(currentData);

        return reply;
    }

    @Override
    public VirtualSensorObject RequestSensorData(String sensorName) {
        VirtualSensorObject reply = new SimpleVirtualSensorObject();
        reply.name = sensorName;

        if (currentData!=null){
            reply = convertToVirtual(currentData);
        }
        else{
            reply.samples = new SensorSample[] {
                    new SensorSample() {{
                        sampleDateTime = LocalDateTime.now().toString();
                        value = random.nextInt(50);
                    }}
            };
        }


        return reply;
    }

    @Override
    public void recordSensorData(SensorObject sensorObject) {

        currentData = sensorObject;
    }

    @Override
    public void recordAggregateSensorData(VirtualSensorObject sensorObject) {

    }

    public void recordAggregateSensorData(SensorObject sensorObject){
        currentData = sensorObject;
    }

    @Override
    public void update_sensor(SensorObject toChange) {

    }

    @Override
    public void assign_user_to_sensor(String serialNumber, String username) {}

    @Override
    public void Remove_sensor_access(String[] username, String sensorID) {

    }

    ;


    @Override
    public TicketObject[] GetTickets() {
        return new TicketObject[0];
    }

    @Override
    public TicketObject[] GetTickets(String userID) {
        return new TicketObject[0];
    }

    @Override
    public void assignTicket(TicketObject toAssign, String username) {

    }

    @Override
    public void RecordTicket(TicketObject toRecord) {

    }

    @Override
    public void updateTicket(int ticketID, int ticketState) {

    }
    @Override
    public void RecordAggregateSensor(SensorObject toRecord) {

    }
    @Override
    public TicketObject[] get_all_tickets() {
        return new TicketObject[0];
    }

    @Override
    public void delete_ticket(int ticketID) {

    }

    @Override
    public VirtualSensorObject[] RequestSensorList(String userName){
        VirtualSensorObject[] result = new VirtualSensorObject[3];

        for (int i= 0; i< 3; i++){
            VirtualSensorObject reply = new SimpleVirtualSensorObject();

            reply.name = "sensor "+i;
            reply.samples = null;
            reply.state = SensorState.ActiveFunctioning;
            reply.type = SensorType.fromValue(i%2);
            reply.description = "This is not a description, i dont care what you say, it not!";
            reply.location = "Mars Satellite Base "+i;
            reply.sampleRate = 5;
            result[i] = reply;
        }
        return result;
    }

    public String getUserRole(String username, String password){
        return "";
    }

    @Override
    public void addUser(UserObject toAdd) {

    }

    @Override
    public void Update_Schedule(String username, String[] schedule) {

    }

    @Override
    public String Get_Schedule(String username) {
        return null;
    }

    @Override

    public UserSchedule[] Get_all_schedules() {
        return new UserSchedule[0];
    }

    @Override
    public UserObject[] Get_Researchers() {
        return new UserObject[0];
    }

    @Override
    public UserObject[] Get_Researchers(String serialNumber) {
        return new UserObject[0];
    }

    @Override
    public String GetTicketState(String serialNumber) {
        return null;
    }

    @Override
    public UserObject get_user_profile(String username, String password) {
        return null;
    }

    private VirtualSensorObject convertToVirtual(SensorObject toConvert){
        VirtualSensorObject reply = new SimpleVirtualSensorObject();

        reply.name = toConvert.name;
        reply.samples = toConvert.samples;
        reply.state = toConvert.state;
        reply.type = toConvert.type;
        reply.description = toConvert.description;
        reply.location = toConvert.location;
        reply.sampleRate = toConvert.sampleRate;
        return reply;
    }
}
