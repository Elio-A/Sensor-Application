using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Communications;
using System;

public class CommunicationsManager : MonoBehaviour
{
    private static CommunicationsManager _instance;

    private void Awake()
    {
        _instance = this;
    }
   
    public static void SendCommand(string message, Promise<Command> result)
    {
        _instance.GetComponentInChildren<ForwardingManager>().SendCommand(message, result);
    }

}
