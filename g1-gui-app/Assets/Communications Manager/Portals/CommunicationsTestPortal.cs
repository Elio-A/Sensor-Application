using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;

public class CommunicationsTestPortal : MonoBehaviour, ICommunicationsPortal
{
    public IEnumerator AssignTicket(TicketObject ticketObject, Promise<TicketObject> result)
    {
        throw new System.NotImplementedException();
    }

    public IEnumerator AssignUsers(string sensorID, string[] users, Promise<string> s)
    {
        throw new System.NotImplementedException();
    }

    public IEnumerator GetCurrentSensorData(string sensorId, Promise<VirtualSensorData> result)
    {
        VirtualSensorData temp = SensorGenerator.GetRandomSensor(1, 1, 0);
        temp.name = sensorId;
        result.value = temp;
        result.done= true;
        yield return null;
    }

    public IEnumerator GetFullHistoricSensorData(string sensorId, string startDate, string endDate, Promise<VirtualSensorData> result) 
    {
        yield return null;
        result.value = SensorGenerator.GetRandomSensor(50,10,1);
    }

    public IEnumerator GetSchedule(string username, Promise<string> s)
    {
        throw new System.NotImplementedException();
    }

    public IEnumerator GetSemiHistoricSensorData(string sensorId, string startDate, Promise<VirtualSensorData> result)
    {
        yield return null;
        result.value = SensorGenerator.GetRandomSensor(50, 10, 1);
    }
    List<VirtualSensorData> tempSamples;
    public IEnumerator GetSensorList(string userID, Promise<VirtualSensorData[]> result)
    {

            yield return null;
            result.value = SensorGenerator.GetRandomSensorArray(25, 25);
            tempSamples = new(result.value);
       
    }

    public IEnumerator GetTicketList(string userId, Promise<TicketObject[]> ticketList)
    {
        throw new System.NotImplementedException();
    }

    public IEnumerator GetUsers(Promise<UserObject[]> users)
    {
        throw new System.NotImplementedException();
    }

    public IEnumerator GetUsers(string sensorID, Promise<UserObject[]> users)
    {
        throw new System.NotImplementedException();
    }

    public IEnumerator SaveTicketStatus(TicketObject ticketObject, Promise<string> s)
    {
        throw new System.NotImplementedException();
    }

    public IEnumerator SendSchedule(string username, string message, Promise<string> s)
    {
        throw new System.NotImplementedException();
    }

    public IEnumerator VerifyLoginDetails(string username, string password, Promise<string> user)
    {
        throw new System.NotImplementedException();
    }
    public IEnumerator SendAggregateData(string sensorId, string nickName, string max, string min, string sampleRate, string equation, string type, Promise<string> s)
    {
        throw new System.NotImplementedException();
    }

    public IEnumerator DeleteTicket(string ticketID, Promise<string> s)
    {
        throw new NotImplementedException();
    }

    public IEnumerator ChangeSensorValue(VirtualSensorData sensor, Promise<string> s)
    {
        throw new NotImplementedException();
    }

    public IEnumerator GetTicketList(Promise<TicketObject[]> ticketList)
    {
        throw new NotImplementedException();
    }

    public IEnumerator GetTicketStatus(string sensorID, Promise<string> status)
    {
        throw new NotImplementedException();
    }
}
