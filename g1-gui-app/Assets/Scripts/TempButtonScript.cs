using framework;
using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class TempButtonScript : MonoBehaviour
{
    private Button startButton;
    public TMP_Text info;
    private bool stay;
    public GameObject widowPrefab;
    private float timer = 0f;
    public float secondsPerIncrement = 1.0f;
    public static Command recieved;
    public bool finished = false;
    HandleJson jsonHandler;
    VirtualSensorData sensorData;
    GameObject go;
    public TMP_FontAsset newFont;
    public string sensorName;

    private void Awake()
    {
        startButton = GetComponent<Button>();
    }

    // Start is called before the first frame update
    private void Start()
    {
        startButton.onClick.AddListener(onButtonClick);
    }

    // Update is called once per frame
    void Update()
    {
        timer += Time.deltaTime;
        if (timer >= secondsPerIncrement)
        {
            if (finished)
            {
                //CommunicationsManager.SendCommand("real time;" + sensorName);
            }
            timer = 0f;
        }
    }
  
    public void onButtonClick()
    {
        go = WindowContainer.Instance.LoadWindow(widowPrefab);
        jsonHandler = new HandleJson();
        //CommunicationsManager.onMessageRecieved.Subscribe(UpdateData);
        go.GetComponent<IWindow>().Show();
        finished = true;

    }

    Transform GetDataTransform(String objectname)
    {
        Transform parentObject = go.GetComponent<ElementComponents>().tickerValues.transform;
        return parentObject;
    }

    GameObject CreateText(Transform canvas_transform, float x, float y, string text_to_print)
    {
        GameObject UItextGO = new GameObject("Text");
        UItextGO.transform.SetParent(canvas_transform);

        RectTransform trans = UItextGO.AddComponent<RectTransform>();
        trans.anchoredPosition = new Vector3(x, y, 0f);

        TMP_Text text = UItextGO.AddComponent<TextMeshProUGUI>();
        text.text = text_to_print;
        text.font = newFont;
        text.fontSize = 20;
        text.alignment = TextAlignmentOptions.Left;

        return UItextGO;
    }

    public void UpdateData(Command newCommand)
    {
        recieved = newCommand;
        sensorData = jsonHandler.DeserializeSingleVirtualSensor(recieved.message);
        SensorSample sample = sensorData.samples[0];
        displayInfo();
        //go.GetComponent<ElementComponents>().tmp.text = sensorData.toString();
        displayData(sample.value + "");
        //Debug.Log(sample.value + "   " + sample.sampleDateTime);
    }

    public void displayData(string message)
    {
        Transform dataObj = GetDataTransform("TickerValue");
        CreateText(dataObj, 0f, 170f, message);
        CreateText(dataObj, 0f, 170f, message);
        updatePosition(dataObj, 30f);
    }

    //updating the data position
    void updatePosition(Transform obj, float offset)
    {
        for (int i = 0; i < obj.childCount; i++)
        {
            Transform c = obj.GetChild(i);
            RectTransform rectTransform = c.GetComponent<RectTransform>();
            rectTransform.anchoredPosition += new Vector2(0, -offset);

        }
    }

    void displayInfo(){

    }
}
