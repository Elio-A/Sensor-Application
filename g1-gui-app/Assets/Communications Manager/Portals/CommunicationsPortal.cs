using framework;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CommunicationsPortal : MonoBehaviour, ICommunicationsPortal
{

    CommunicationsLivePortal livePortal;
    CommunicationsTestPortal testPortal;
    ICommunicationsPortal currentPotal;
    public static CommunicationsPortal Instance;

    private void Awake()
    {
        livePortal = GetComponent<CommunicationsLivePortal>();
        testPortal = GetComponent<CommunicationsTestPortal>();
        Instance = this;
    }

    private void Start()
    {
        if (TestingManager.InTestMode)
            currentPotal = testPortal;
        else
            currentPotal = livePortal;

    }

    public IEnumerator GetSensorList(string userID, Promise<VirtualSensorData[]> sensorList)
    {
        yield return StartCoroutine(currentPotal.GetSensorList(userID, sensorList));
    }

    public IEnumerator GetCurrentSensorData(string sensorId, Promise<VirtualSensorData> sensor)
    {
        yield return StartCoroutine(currentPotal.GetCurrentSensorData(sensorId, sensor));
    }

    public IEnumerator GetSemiHistoricSensorData(string sensorId, string startDate, Promise<VirtualSensorData> sensor)
    {
        yield return StartCoroutine(currentPotal.GetSemiHistoricSensorData(sensorId, startDate, sensor));
    }

    public IEnumerator GetFullHistoricSensorData(string sensorId, string startDate, string endDate, Promise<VirtualSensorData> sensor)
    {
        yield return StartCoroutine(currentPotal.GetFullHistoricSensorData(sensorId, startDate, endDate, sensor));
    }

    public IEnumerator VerifyLoginDetails(string username, string password, Promise<string> user)
    {
        yield return StartCoroutine(currentPotal.VerifyLoginDetails(username, password, user));
    }

    public IEnumerator GetTicketList(string userId, Promise<TicketObject[]> ticketList)
    {
        yield return StartCoroutine(currentPotal.GetTicketList(userId, ticketList));
    }

    public IEnumerator GetSchedule(string username, Promise<string> schedule)
    {
        yield return StartCoroutine(currentPotal.GetSchedule(username, schedule));
    }

    public IEnumerator SendSchedule(string username, string message, Promise<string> s)
    {
        yield return StartCoroutine(currentPotal.SendSchedule(username, message, s));
    }

    public IEnumerator SaveTicketStatus(TicketObject ticketObject, Promise<string> s)
    {
        yield return StartCoroutine(currentPotal.SaveTicketStatus(ticketObject, s));
    }

    public IEnumerator GetUsers(Promise<UserObject[]> users)
    {
        yield return StartCoroutine(currentPotal.GetUsers(users));
    }

    public IEnumerator GetUsers(string sensorID, Promise<UserObject[]> users)
    {
        yield return StartCoroutine(currentPotal.GetUsers(sensorID, users));
    }

    public IEnumerator AssignUsers(string sensorID, string[] users, Promise<string> s)
    {
        yield return StartCoroutine(currentPotal.AssignUsers(sensorID, users, s));
    }
    public IEnumerator SendAggregateData(string sensorId, string nickName, string max, string min, string sampleRate, string equation, string type, Promise<string> s)
    {
        yield return StartCoroutine(currentPotal.SendAggregateData(sensorId, nickName, max, min, sampleRate, equation, type, s));
    }

    public IEnumerator DeleteTicket(string ticketID, Promise<string> s)
    {
        yield return StartCoroutine(currentPotal.DeleteTicket(ticketID, s));
    }

    public IEnumerator ChangeSensorValue(VirtualSensorData sensor, Promise<string> s)
    {
        yield return StartCoroutine(currentPotal.ChangeSensorValue(sensor, s));
    }

    public IEnumerator GetTicketList(Promise<TicketObject[]> ticketList)
    {
        yield return StartCoroutine(currentPotal.GetTicketList(ticketList));
    }

    public IEnumerator GetTicketStatus(string sensorID, Promise<string> status)
    {
        yield return StartCoroutine(currentPotal.GetTicketStatus(sensorID, status));
    }
}
