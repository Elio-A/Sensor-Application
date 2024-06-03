using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[Serializable]
public class TicketObject
{
    public int ticketID;
    public string fieldNotes;
    public VirtualSensorData sensorInfo;
    public string creationDate;
    public string ticketState;
    public string assignedUserID;
    public int assignedTimeSlot;

}
