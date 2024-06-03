using System.Collections;
using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;

public class SensorDataController : MonoBehaviour
{
    public static List<DataEntryController> sensorLiveDataEntries = new List<DataEntryController>();
    [SerializeField]
    GameObject sensorDataEntryPrefab;
    [SerializeField]
    private Transform container;
    private VirtualSensorData[] _sensor;



    public void Initialize(VirtualSensorData[] sensors, List<Color> colours, bool historical, string start, string end)
    {
        _sensor = sensors;
        Color[] graphColor = colours.ToArray();
        for (int i = 0; i < _sensor.Length; i++)
        {
            if (historical)
            {
                AddEntryHistoric(_sensor[i], graphColor[i], start, end);                
            }
            else 
            {
                AddEntry(_sensor[i], graphColor[i]);
            }
            
        }
    }

    private void AddEntry(VirtualSensorData sensor, Color color)
    {
        GameObject gO = Instantiate(sensorDataEntryPrefab, container);
        gO.GetComponent<DataEntryController>().Initialize(sensor, color);
    }
    private void AddEntryHistoric(VirtualSensorData sensor, Color color, string start, string end)
    {
        GameObject gO = Instantiate(sensorDataEntryPrefab, container);
        gO.GetComponent<HistoricalDataEntryController>().Initialize(sensor, color, start, end);
    }

    public void UpdateValues(VirtualSensorData[] sensors)
    {
        DataEntryController[] controller = sensorLiveDataEntries.ToArray();
        _sensor = sensors;
        for (int i = 0; i < _sensor.Length; i++)
        {
            controller[i].UpdateThings(_sensor[i]);
        }
    }


}
