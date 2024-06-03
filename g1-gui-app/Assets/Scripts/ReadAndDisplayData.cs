using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;


public class ReadAndDisplayData : MonoBehaviour
{
    private float timer = 0f;
    public float secondsPerIncrement = 1.0f;
    public static Command recieved;
    public static bool connection;
    HandleJson jsonHandler;
    VirtualSensorData sensorData;
    public TMP_FontAsset newFont;

    // Start is called before the first frame update
    void Start()
    {
        connection = false;
        jsonHandler = new HandleJson();
       // CommunicationsManager.onConnectionEstablished.Subscribe(AllowSending);
      //  CommunicationsManager.onMessageRecieved.Subscribe(UpdateData);
    }

    // Update is called once per frame
    void Update()
    {
        timer += Time.deltaTime;
        if (timer >= secondsPerIncrement)
        {
            if(connection)
            {
              //  CommunicationsManager.SendCommand("real time;DefaultSensor");
            }
            timer = 0f;
        }
    }

    Transform GetDataTransform(String objectname)
    {
        GameObject go = GameObject.Find(objectname);
        if (go != null)
        {
            return go.transform;
        }
        else
        {
            Debug.Log(objectname + " not found");
            return null;
        }
    }

    GameObject CreateText(Transform canvas_transform, float x, float y, string text_to_print)
    {
        GameObject UItextGO = new GameObject("Text");
        UItextGO.transform.SetParent(canvas_transform);

        RectTransform trans = UItextGO.AddComponent<RectTransform>();
        // trans.position = new Vector3(x, y, 0f);

        TMP_Text text = UItextGO.AddComponent<TextMeshProUGUI>();
        // text.font = newFont;
        // text.alignment = TextAlignmentOptions.Left;
        // text.text = text_to_print;

        return UItextGO;
    }

    public void UpdateData(Command newCommand)
    {
        recieved = newCommand;
        sensorData = jsonHandler.DeserializeSingleVirtualSensor(recieved.message);
        SensorSample sample = sensorData.samples[0];
       // displayData(sample.value + "   " +  sample.sampleDateTime);
        Debug.Log(sample.value + "   " + sample.sampleDateTime);
    }

    public void AllowSending()
    {
        connection = true;
    }

    public void displayData(string message)
    {
        Transform dataObj = GetDataTransform("DataObject");
        CreateText(dataObj, 355.0f, 340.0f, message);
        updatePosition(dataObj, 50f);
    }

    //updating the data position
    void updatePosition(Transform obj, float offset)
    {
        for(int i=0; i<obj.childCount; i++)
        {
            Transform c = obj.GetChild(i);
            RectTransform rectTransform = c.GetComponent<RectTransform>();
            rectTransform.anchoredPosition += new Vector2(0, -offset);
            
        }
    }
    private void falseMethod(Command newCommand)
    {
        
    }
}
