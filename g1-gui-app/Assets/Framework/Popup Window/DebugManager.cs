using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using framework;
using System.Linq.Expressions;

public class DebugManager : MonoBehaviour
{
    public GameObject popupWindow;
    private static DebugManager _instance;

    public bool testMe;
    private void Awake()
    {
        _instance = this;
    }
    private void Update()
    {
        if (testMe)
        {
            testMe = false;
            Test();
        }
    }
    private void OnEnable()
    {
        Application.logMessageReceived += HandleLog;
    }

    private void OnDisable()
    {
        Application.logMessageReceived -= HandleLog;
    }
    public static void DisplayLog(string name, string message)
    {
        GameObject gO = LoginSceneWindowContainer.Instance.LoadWindow(_instance.popupWindow);
        gO.GetComponent<PopUpWindowController>().DisplayMessage(name, message);
        gO.GetComponent<IWindow>().Show();
    }

    void HandleLog(string logString, string stackTrace, LogType type)
    {
        if (type == LogType.Exception)
        {
            DisplayLog("ERROR", "Error:\n" + logString);
        }
    }
    void Test()
    {
        throw new Exception("WOAH DUDE-> HOLD UP!");
    }
}
