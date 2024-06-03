package DataManagementSystem;
import SharedDataTypes.*;
import SharedDataTypes.SensorObjects.SimpleSensorObject;
import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorObjects.SimpleVirtualSensorObject;
import SharedDataTypes.SensorObjects.VirtualSensorObject;
import org.apache.tomcat.jni.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataManager implements IDataManagementSystem{
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
        reply.samples = DBAccess.getSensor_reading(sensorName, start, end);

        return reply;
    }

    @Override
    public VirtualSensorObject RequestSensorData(String sensorName, String start) {
        VirtualSensorObject reply = new SimpleVirtualSensorObject();

        reply.name = sensorName;
        reply.samples = DBAccess.getSensor_reading(sensorName, start, "");

        return reply;
    }
    @Override
    public VirtualSensorObject[] RequestSensorList(String userName){
        ArrayList<SimpleSensorObject> resultSensors = null;
        if(DBAccess.check_role(userName) == 0){
            resultSensors = DBAccess.listSensors();
        }
        else {
            resultSensors = DBAccess.request_user_sensors(userName);
        }

        VirtualSensorObject[] result = new VirtualSensorObject[resultSensors.size()];

        for (int i=0; i<resultSensors.size(); i++){
            result[i] = convertToVirtual(resultSensors.get(i));
        }

        return result;
    }

    @Override
    public void update_sensor(SensorObject toChange){
        DBAccess.update_sensor(toChange);
    }

    @Override
    public VirtualSensorObject RequestSensorData(String sensorName) {
        VirtualSensorObject reply = new SimpleVirtualSensorObject();
        reply.name = sensorName;

        if (currentData!=null){
            reply = convertToVirtual(currentData);
        }
        else{
            reply.samples = DBAccess.getSensor_reading(sensorName, "", "");
        }

        return reply;
    }

    @Override
    public void delete_ticket(int ticketID){
        DBAccess.delete_ticket(ticketID);
    }

    @Override
    public TicketObject[] get_all_tickets(){
        return DBAccess.list_tickets();
    }

    @Override
    public void recordSensorData(SensorObject sensorObject) {
        SimpleSensorObject temp_obj = DBAccess.check_sensor(sensorObject.name);
        if (temp_obj.name==null) {

            //adding sensor
            DBAccess.addSensor(sensorObject.name,
                    sensorObject.nickName,
                    sensorObject.sampleRate,
                    sensorObject.type.getValue(),
                    sensorObject.state.getValue(),
                    sensorObject.description,
                    sensorObject.location,
                    sensorObject.max,
                    sensorObject.min);
        }
        else
            DBAccess.update_sensor(sensorObject);

        //adding sensor data
        for(int i=0; i<sensorObject.samples.length; i++) {
            String time = sensorObject.samples[i].sampleDateTime;
            DBAccess.addSensor_reading(sensorObject.name,
                    sensorObject.samples[i].value,
                    time);
        }
        currentData = sensorObject;
    }

    public void recordAggregateSensorData(VirtualSensorObject sensorObject){
        //adding sensor
        DBAccess.addSensor(sensorObject.name,
                sensorObject.nickName,
                sensorObject.sampleRate,
                sensorObject.type.getValue(),
                sensorObject.state.getValue(),
                sensorObject.description,
                sensorObject.location,
                sensorObject.max,
                sensorObject.min);
    }

    @Override
    public void addUser(UserObject toAdd){
        DBAccess.add_user_profile(toAdd);
        DBAccess.add_user(toAdd.userName, toAdd.password);
    }

    @Override
    public UserObject get_user_profile(String username, String password){
        return DBAccess.get_user_profile(username, password);
    }

    @Override
    public String GetTicketState(String serialNumber) {
        ArrayList<TicketObject>List = new ArrayList<>();
        ArrayList<TicketObject>tryList = DBAccess.get_ticket(serialNumber);

        if(tryList!=null){
            List = tryList;
        }
        else{
            System.out.println("\n\n\n\nIT WAS FUCKING NULL AGAIN!\n\n\n\n");
        }

        if(!(List.isEmpty())) {
            for (int i = 0; i < List.size(); i++) {
                if (List.get(i).ticketState.getValue() != 3) {
                    return List.get(i).ticketState.name();
                }
            }
        }
        return "none";
    }

    //retrieving tickets with no assigned users //////////no need
    @Override
    public TicketObject[] GetTickets() {
        return DBAccess.get_nonassigned_sensor_tickets();
    }

    @Override
    public TicketObject[] GetTickets(String username) {
        return DBAccess.get_user_tickets(username);
    }

    @Override
    public void updateTicket(int ticketID, int ticketState){
        DBAccess.update_ticket_status(ticketID, ticketState);
    }

    @Override
    public void assignTicket(TicketObject toAssign, String username){
        DBAccess.assign_user_to_ticket(username, toAssign);
    }

    @Override
    public void RecordAggregateSensor(SensorObject toRecord) {

    }


    @Override
    public void RecordTicket(TicketObject toRecord){
        DBAccess.add_ticket(toRecord);
    }

    @Override
    public String getUserRole(String username, String password){
        int roleInt =DBAccess.get_user_authority(username, password);
        System.out.println("THE ROLE int RETURNED WAS: "+roleInt);

        String role = DBAccess.get_role_group(roleInt);
        System.out.println("THE ROLE RETURNED WAS: "+role);

        return role;
    }

    @Override
    //get all researcher in system(no inputs / return UserObject[])
    public UserObject[] Get_Researchers(){ //return researcher
        return DBAccess.Get_Researcher();
    }

    @Override
    public UserObject[] Get_Researchers(String serialNumber){ //all researchers assigned to a specific sensor
        return DBAccess.Get_Researcher(serialNumber);
    }

    @Override
    //Method to Send_schedule(input username / schedule (String array)) (create automatic schedule when adding a technician)
    public void Update_Schedule(String username, String[] schedule){
        DBAccess.update_Schedule(username, schedule);
    }

    @Override
    //method for schedule (input username / return schedule as string "1;0;1;1;1;0;)
    public String Get_Schedule(String username){
        return DBAccess.Get_Schedule(username);
    }

    @Override
    public UserSchedule[] Get_all_schedules(){
        return DBAccess.get_all_schedules();
    }

    @Override
    public void assign_user_to_sensor(String username, String sensorID){
        DBAccess.add_sensor_access(sensorID, username);
    }

    @Override
    public void Remove_sensor_access(String[] username, String sensorID){
        //remove all users assigned to a sensor
        //assign researchers in the string array only
        DBAccess.delete_all_sensor_access(sensorID);
        for(int i=0; i< username.length; i++){
            assign_user_to_sensor(username[i], sensorID);
        }
    }

    private VirtualSensorObject convertToVirtual(SensorObject toConvert){
        VirtualSensorObject reply = new SimpleVirtualSensorObject();

        reply.name = toConvert.name;
        reply.nickName = toConvert.nickName;
        reply.samples = toConvert.samples;
        reply.state = toConvert.state;
        reply.type = toConvert.type;
        reply.description = toConvert.description;
        reply.location = toConvert.location;
        reply.sampleRate = toConvert.sampleRate;
        reply.max = toConvert.max;
        reply.min = toConvert.min;
        return reply;
    }
}
