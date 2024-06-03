package DataManagementSystem;

import InternalMessaging.ISystemInitialization;
import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorObjects.VirtualSensorObject;
import SharedDataTypes.TicketObject;
import SharedDataTypes.TicketState;
import SharedDataTypes.UserObject;
import SharedDataTypes.UserSchedule;

import java.time.ZonedDateTime;

public interface IDataManagementSystem extends ISystemInitialization {

    /**
     * request data with sensorName, and an interval of time
     * @param sensorName serial number of a sensor
     * @param start start time
     * @param end end time
     * @return Virtual sensor
     */
    public VirtualSensorObject RequestSensorData(String sensorName, String start, String end);


    /**
     * request data starting a specific time till the most recent one using a serial number
     * @param sensorName serial number of a sensor
     * @param start start time
     * @return Virtual sensor
     */
    public VirtualSensorObject RequestSensorData(String sensorName, String start);


    /**
     * Request data of a sensor using serial number
     * @param sensorName serial Number of a sensor
     * @return virtual sensor
     */
    public VirtualSensorObject RequestSensorData(String sensorName);


    /**
     * request sensor list that a user has access to
     * @param userName username
     * @return array of virtual sensors a user has access to
     */
    public VirtualSensorObject[] RequestSensorList(String userName);


    /**
     * Input sensor data into the DB
     * @param sensorObject Sensor to add to the Database
     */
    public void recordSensorData(SensorObject sensorObject);


    /**
     * Input aggregate sensor into the DB
     * @param sensorObject aggregate sensor object to be added
     */
    public void recordAggregateSensorData(VirtualSensorObject sensorObject);


    /**
     * To update a sensor in the Database
     * @param toChange Sensor to update
     */
    public void update_sensor(SensorObject toChange);


    /**
     * Get the role of a user with username and password
     * @param username used to retrieve the role of that specific user
     * @param password verifying identity
     * @return string of the role of a user
     */
    public String getUserRole(String username, String password);


    /**
     * add a new user to Database
     * @param toAdd the user to input to the Database
     */
    public void addUser(UserObject toAdd);


    /**
     * update schedule of a user
     * @param username username to update schedule for
     * @param schedule for security reasons, used to verify identity
     */
    public void Update_Schedule(String username, String[] schedule);


    /**
     * Get schedule of a specific user
     * @param username used to specify a user
     * @return String of schedule of form 0;0;0;1;1;1...
     */
    public String Get_Schedule(String username);

    public UserSchedule[] Get_all_schedules();


    /**
     * retrieve all researchers
     * @return array of researchers in the Database
     */
    public UserObject[] Get_Researchers();


    /**
     * retrieve all researchers assigned to a specific sensor
     * @param serialNumber Specifying which sensor the users have access to
     * @return list of user objects assigned to a sensor
     */
    public UserObject[] Get_Researchers(String serialNumber);


    /**
     * Get the ticket state of a sensor using serial Number
     * @param serialNumber to specify which ticket of the sensor we are trying to retrieve
     * @return string containing the ticket state
     */
    public String GetTicketState(String serialNumber);


    /**
     * request the profile of a user
     * @param username check credentials
     * @param password check credentials
     * @return userObject containing user_profile of the username
     */
    public UserObject get_user_profile(String username, String password);


    /**
     * assign a sensor to a user
     * @param serialNumber serial number of the sensor a user is being assigned to
     * @param username name of the user being assigned to a sensor
     */
    public void assign_user_to_sensor(String serialNumber, String username);


    /**
     * remove access to a user
     * @param username username to remove access
     * @param sensorID serial number of the sensor
     */
    public void Remove_sensor_access(String[] username, String sensorID);


    /**
     * get a list of tickets that are not assigned to anyone
     * @return array of tickets
     */
    public TicketObject[] GetTickets();


    /**
     * Get the ticket a user is assigned to
     * @param username user used to retrieve the sensors for
     * @return array of tickets assigned to a user
     */
    public TicketObject[] GetTickets(String username);


    /**
     * Assign a ticket to a user
     * @param toAssign ticket being assigned to a user
     * @param username username used to assign sensor to
     */
    public void assignTicket(TicketObject toAssign, String username);


    /**
     * add sensor aggregation
     * @param toRecord sensor aggregation being added
     */
    public void RecordAggregateSensor(SensorObject toRecord);


    /**
     * Add a new ticket to the DB
     * @param toRecord Ticket being recorded on the Database
     */
    public void RecordTicket(TicketObject toRecord);


    /**
     * update state of a ticket
     * @param ticketID Ticket being updated
     * @param ticketState New ticket state
     */
    public void updateTicket(int ticketID, int ticketState);


    /**
     * retrieve all tickets in the DB
     * @return array of tickets in the system
     */
    public TicketObject[] get_all_tickets();


    /**
     * Delete a specific Ticket
     * @param ticketID ticket being deleted
     */
    public void delete_ticket(int ticketID);
}
