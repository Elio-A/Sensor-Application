package SharedDataTypes;

import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorObjects.VirtualSensorObject;

public class TicketObject {

    public int ticketID;
    public String fieldNotes;
    public SensorObject sensorInfo;
    public String creationDate;
    public TicketState ticketState;
    public String assignedUserID;
    public int assignedTimeSlot;
    public boolean isSent = false;


}
