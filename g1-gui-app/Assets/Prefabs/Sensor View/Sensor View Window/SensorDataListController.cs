using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SensorDataListController : MonoBehaviour
{
    [SerializeField]
    GameObject sensorDataEntryPrefab;
    [SerializeField]
    private Transform container;
    private VirtualSensorData _sensor;



    public void Initialize(VirtualSensorData sensors)
    {
        _sensor = sensors;
        SensorSample[] sensorSamples = _sensor.samples;
        for (int i = 0; i < sensorSamples.Length; i++)
        {
            AddEntry(sensorSamples[i]);
        }
    }

    private void AddEntry(SensorSample sensorData)
    {
        GameObject gO = Instantiate(sensorDataEntryPrefab, container);
        gO.GetComponent<SensorDataEntryController>().Initialize(sensorData);
    }
}
