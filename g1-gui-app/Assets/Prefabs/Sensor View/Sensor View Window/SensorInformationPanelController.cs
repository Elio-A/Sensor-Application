using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class SensorInformationPanelController : MonoBehaviour
{
    private VirtualSensorData _sensor;
    [SerializeField]
    private TextMeshProUGUI nameField;
    [SerializeField]
    private TextMeshProUGUI typeField;
    [SerializeField]
    private TextMeshProUGUI samplingRateField;
    [SerializeField]
    private TextMeshProUGUI locationField;

    public void Initialize(VirtualSensorData sensorData)
    {
        _sensor = sensorData;
        nameField.text = _sensor.name;
        nameField.ForceMeshUpdate();
        typeField.text = _sensor.type;
        typeField.ForceMeshUpdate();
        samplingRateField.text = _sensor.sampleRate.ToString() + " seconds";
        samplingRateField.ForceMeshUpdate();
        locationField.text = _sensor.location;
        locationField.ForceMeshUpdate();
    }

}
