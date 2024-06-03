using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class DataEntryController : MonoBehaviour
{
    public VirtualSensorData _sensor;
    [SerializeField]
    public TextMeshProUGUI timeStamp;
    [SerializeField]
    public TextMeshProUGUI readings;
    [SerializeField]
    public TextMeshProUGUI sensorName;
    [SerializeField]
    public TextMeshProUGUI location;
    [SerializeField]
    private Image panel;

    public void Initialize(VirtualSensorData sensor, Color color)
    {
        _sensor = sensor;
        SensorDataController.sensorLiveDataEntries.Add(this);
        sensorName.text = _sensor.nickName;
        sensorName.ForceMeshUpdate();
        timeStamp.text = _sensor.samples[0].sampleDateTime;
        timeStamp.ForceMeshUpdate();
        location.text = _sensor.location;
        if(_sensor.type == "Energy")
        {
            readings.text = _sensor.samples[0].value.ToString("0.00") + " KW/hr";
        }else if(_sensor.type == "Water")
        {
            readings.text = _sensor.samples[0].value.ToString("0.00") + " L/hr";
        }
        readings.ForceMeshUpdate();
        panel.color = color;
    }

    public void UpdateThings(VirtualSensorData sensor)
    {
        _sensor = sensor;
        timeStamp.text = _sensor.samples[0].sampleDateTime;
        timeStamp.ForceMeshUpdate();
        if (_sensor.type == "Energy")
        {
            readings.text = _sensor.samples[0].value.ToString("0.00") + " KW/hr";
        }
        else if (_sensor.type == "Water")
        {
            readings.text = _sensor.samples[0].value.ToString("0.00") + " L/hr";
        }
        readings.ForceMeshUpdate();
    }
}
