using System.Collections;
using System.Collections.Generic;
using UnityEditor.VersionControl;
using UnityEngine;

public class CommunicationsLivePortal : MonoBehaviour, ICommunicationsPortal
{
    HandleJson jsonHandler;
    private void Awake()
    {
        jsonHandler = new();
    }

    public IEnumerator GetSensorList(string userID, Promise<VirtualSensorData[]> sensorList)
    {
        string message = "sensor list;"+userID;
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);

        string jsonResult = result.value.message;

        VirtualSensorData[] sensors = jsonHandler.DeserializeVirtualSensorArray(jsonResult);
        sensorList.value = sensors;
        sensorList.done = true;
    }

    public IEnumerator GetCurrentSensorData(string sensorId, Promise<VirtualSensorData> sensor)
    {
        string message = "real time;" + sensorId;
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);
        VirtualSensorData sensorResult = null;
        string jsonResult = result.value.message;
        if (jsonResult.Contains(".DataBufferLimitException"))
        {
            DebugManager.DisplayLog("TOO MASSIVE!", "The amount of data you have requested is simply too MASSIVE.\nPlease choose a smaller abount of data");
        }
        else
        {
            sensorResult = jsonHandler.DeserializeSingleVirtualSensor(jsonResult);
        }
        sensor.value = sensorResult;
        sensor.done = true;
    }

    public IEnumerator GetSemiHistoricSensorData(string sensorId, string startDate, Promise<VirtualSensorData> sensor)
    {
        string message = "semi historic;" + sensorId + ";" + startDate;
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);
        VirtualSensorData sensorResult = null;
        string jsonResult = result.value.message;
        Debug.Log("!!!!!!!!!GetSemiHistoricSensorData: "+jsonResult);
        if (jsonResult.Contains(".DataBufferLimitException"))
        {
            DebugManager.DisplayLog("TOO MASSIVE!", "The amount of data you have requested is simply too MASSIVE.\nPlease choose a smaller abount of data");
        }
        else
        {
            sensorResult = jsonHandler.DeserializeSingleVirtualSensor(jsonResult);
        }
        sensor.value = sensorResult;
        sensor.done = true;
    }

    public IEnumerator GetFullHistoricSensorData(string sensorId, string startDate, string endDate, Promise<VirtualSensorData> sensor)
    {
        string message = "full historic;" + sensorId + ";" + startDate + ";" + endDate;
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);

        VirtualSensorData sensorResult = null;
        string jsonResult = result.value.message;
        if (jsonResult.Contains(".DataBufferLimitException"))
        {
            DebugManager.DisplayLog("TOO MASSIVE!", "The amount of data you have requested is simply too MASSIVE.\nPlease choose a smaller abount of data");
        }
        else
        {
            sensorResult = jsonHandler.DeserializeSingleVirtualSensor(jsonResult);
        }
        sensor.value = sensorResult;
        sensor.done = true;
    }

    public IEnumerator VerifyLoginDetails(string username, string password, Promise<string> user)
    {
        string message = "verify login;" + username + ";" + password;
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);

        string jsonResult = result.value.message;

        user.value = jsonResult;
        user.done = true;
    }

    public IEnumerator GetTicketList(string userId, Promise<TicketObject[]> ticketList)
    {
        string message = "ticket list;" + userId;
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);

        string jsonResult = result.value.message;
        TicketObject[] ticketResult = jsonHandler.DeserializeTicketObjectArray(jsonResult);
        ticketList.value = ticketResult;
        ticketList.done = true;
    }

    public IEnumerator GetTicketList(Promise<TicketObject[]> ticketList)
    {
        string message = "admin ticket list;";
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);

        string jsonResult = result.value.message;
        TicketObject[] ticketResult = jsonHandler.DeserializeTicketObjectArray(jsonResult);
        ticketList.value = ticketResult;
        ticketList.done = true;
    }

    public IEnumerator GetSchedule(string username, Promise<string> schedule)
    {
        string message = "get schedule;" + username;
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);

        string jsonResult = result.value.message;
        schedule.value = jsonResult;
        schedule.done = true;
    }

    public IEnumerator SendSchedule(string username, string message, Promise<string> s)
    {
        string command = "set schedule;" + username + ";" + message;
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(command, result);

        yield return new WaitUntil(() => result.done);
        s.done = true;
    }

    public IEnumerator SaveTicketStatus(TicketObject ticketObject, Promise<string> s)
    {
        string message = "save ticket status;" + ticketObject.ticketID + ";" + ticketObject.ticketState;
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);
        s.done = true;
    }

    public IEnumerator GetUsers(Promise<UserObject[]> users)
    {
        string message = "get all users;";
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);

        string jsonResult = result.value.message;

        users.value = jsonHandler.DeserializeUserObjectArray(jsonResult);
        users.done = true;
    }

    public IEnumerator GetUsers(string sensorID, Promise<UserObject[]> users)
    {
        string message = "get assigned users;" + sensorID;
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);

        string jsonResult = result.value.message;

        users.value = jsonHandler.DeserializeUserObjectArray(jsonResult);
        users.done = true;
    }

    public IEnumerator AssignUsers(string sensorID, string[] users, Promise<string> s)
    {
        string message = "assign users;" + sensorID + ";";
        for(int i = 0;  i < users.Length; i++)
        {
            message += users[i];
            if(i != users.Length - 1)
            {
                message += ":";
            }
        }
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);
        s.done = true;
    }

    public IEnumerator SendAggregateData(string sensorId,string nickName, string max, string min, string sampleRate, string equation, string type, Promise<string> s)
    {
        string message = "aggregate;" + sensorId + ";" + nickName + ";" + max + ";" + min + ";" + sampleRate + ";" + equation + ";" + type;

        Promise<Command> send = new Promise<Command>();
        CommunicationsManager.SendCommand(message, send);
        yield return new WaitUntil(() => send.done);
        s.done = true;

    }

    public IEnumerator DeleteTicket(string ticketID, Promise<string> s)
    {
        string message = "delete ticket;" + ticketID;
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);
        s.done = true;
    }

    public IEnumerator ChangeSensorValue(VirtualSensorData sensor, Promise<string> s)
    {
        string message = "change sensor;";
        string json = jsonHandler.SerializeSingleVirtualSensor(sensor);
        message += json;
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);
        s.done = true;
    }

    public IEnumerator GetTicketStatus(string sensorID, Promise<string> status)
    {
        string message = "get ticket status;" + sensorID;
        Promise<Command> result = new Promise<Command>();

        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);
        status.value = result.value.message;
        status.done = true;
    }
}
