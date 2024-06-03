using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class RetriveHistorical : MonoBehaviour
{
    private Button historyButton;
    CSVWriter csvWriter;
    HandleJson jsonHandler;
    public string sensorName;
    public string startDate;
    public string endDate;
    public static Command recieved;
    VirtualSensorData sensorData;

    private void Awake()
    {
        historyButton = GetComponent<Button>();
    }

    // Start is called before the first frame update
    private void Start()
    {
        historyButton.onClick.AddListener(onButtonClick);
    }

    public void onButtonClick()
    {
        csvWriter = new CSVWriter();
        jsonHandler = new HandleJson();
        //CommunicationsManager.onMessageRecieved.Subscribe(getData);
        //CommunicationsManager.SendCommand("full historic;" + sensorName + ";" + startDate + ";" + endDate);
    }

    public void getData(Command newCommand)
    {
        recieved = newCommand;
        sensorData = jsonHandler.DeserializeSingleVirtualSensor(recieved.message);
        csvWriter.Save(sensorData.samples);        
    }
}
