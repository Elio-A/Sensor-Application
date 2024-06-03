using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Linq;
using System.Text.RegularExpressions;

public class HandleJson
{
    [System.Serializable]
    public class VirtualSensorDataArray
    {
        public VirtualSensorData[] array;
    }

    public VirtualSensorData DeserializeSingleVirtualSensor(string json)
    {
        VirtualSensorData newSensor = JsonUtility.FromJson<VirtualSensorData>(json);
        return newSensor;
    }

    public VirtualSensorData[] DeserializeVirtualSensorArray(string jsonArray)
    {
        Debug.Log("DeserializeVirtualSensorArray:\n\n" + jsonArray);

        // Use Regex to split the string into individual JSON objects
        var matches = Regex.Matches(jsonArray, @"\{.*?\}");
        List<VirtualSensorData> sensorList = new List<VirtualSensorData>();

        foreach (Match match in matches)
        {
            VirtualSensorData sensorData = JsonUtility.FromJson<VirtualSensorData>(match.Value);
            sensorList.Add(sensorData);
        }

        return sensorList.ToArray();
    }

    public string SerializeSingleVirtualSensor(VirtualSensorData newSensor)
    {
        string json = JsonUtility.ToJson(newSensor);
        return json;
    }
    public string SerializeSingleTicketObject(TicketObject ticket)
    {
        string json = JsonUtility.ToJson(ticket);
        return json;
    }

    public TicketObject DeserializeSingleTicketObject(string json)
    {
        TicketObject newTicket = JsonUtility.FromJson<TicketObject>(json);
        return newTicket;
    }
    [Serializable]
    public class TicketArray
    {
        public TicketObject[] tickets;
    }
    public TicketObject[] DeserializeTicketObjectArray(string jsonArray)
    {
        Debug.Log("DeserializeTicketObjectArray:\n\n" + jsonArray);

        // Deserialize the entire JSON array
        TicketArray ticketArray = JsonUtility.FromJson<TicketArray>("{\"tickets\":" + jsonArray + "}");

        return ticketArray.tickets;
    }

    /*  public TicketObject[] DeserializeTicketObjectArray(string jsonArray)
      {
          Debug.Log("DeserializeTicketObjectArray:\n\n" + jsonArray);

          // Use Regex to split the string into individual JSON objects

          var matches = Regex.Matches(jsonArray, @"\{.*?\}");
          List<TicketObject> ticketList = new List<TicketObject>();

          foreach (Match match in matches)
          {
              TicketObject ticketData = JsonUtility.FromJson<TicketObject>(match.Value);
              ticketList.Add(ticketData);
          }

          return ticketList.ToArray();
      }*/
    public UserObject[] DeserializeUserObjectArray(string jsonArray)
    {
        Debug.Log("DeserializeUserObjectArray:\n\n" + jsonArray);

        // Use Regex to split the string into individual JSON objects
        var matches = Regex.Matches(jsonArray, @"\{.*?\}");
        List<UserObject> userList = new List<UserObject>();

        foreach (Match match in matches)
        {
            UserObject userData = JsonUtility.FromJson<UserObject>(match.Value);
            userList.Add(userData);
        }

        return userList.ToArray();
    }
}
