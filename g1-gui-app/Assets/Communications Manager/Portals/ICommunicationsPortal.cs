using System.Collections;
using System.Collections.Generic;
using UnityEngine;
public interface ICommunicationsPortal 
{
    public IEnumerator GetSensorList(string userID, Promise<VirtualSensorData[]> sensorList);
    public IEnumerator GetCurrentSensorData(string sensorId, Promise<VirtualSensorData> sensor);
    public IEnumerator GetSemiHistoricSensorData(string sensorId, string startDate, Promise<VirtualSensorData> sensor);
    public IEnumerator GetFullHistoricSensorData(string sensorId, string startDate, string endDate, Promise<VirtualSensorData> sensor);

    public IEnumerator VerifyLoginDetails(string username, string password, Promise<string> user);
    public IEnumerator GetTicketList(string userId, Promise<TicketObject[]> ticketList);
    public IEnumerator GetTicketList(Promise<TicketObject[]> ticketList);
    public IEnumerator GetSchedule(string username, Promise<string> schedule);
    public IEnumerator SendSchedule(string username, string message, Promise<string> s);
    public IEnumerator SaveTicketStatus(TicketObject ticketObject, Promise<string> s);
    public IEnumerator GetUsers(Promise<UserObject[]> users);
    public IEnumerator GetUsers(string sensorID, Promise<UserObject[]> users);
    public IEnumerator AssignUsers(string sensorID, string[] users, Promise<string> s);
    public IEnumerator SendAggregateData(string sensorId, string nickName, string max, string min, string sampleRate, string equation, string type, Promise<string> s);
    public IEnumerator DeleteTicket(string ticketID, Promise<string> s);
    public IEnumerator ChangeSensorValue(VirtualSensorData sensor, Promise<string> s);
    public IEnumerator GetTicketStatus(string sensorID, Promise<string> status);
}
