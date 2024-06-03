using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class SensorDataEntryController : MonoBehaviour
{
    private SensorSample _sensorSample;
    [SerializeField]
    private TextMeshProUGUI timeStamp;
    [SerializeField]
    private TextMeshProUGUI readings;

    public void Initialize(SensorSample sensorSample)
    {
        _sensorSample = sensorSample;
        timeStamp.text = _sensorSample.sampleDateTime.ToString();
        timeStamp.ForceMeshUpdate();
        readings.text = _sensorSample.value.ToString("0.00");
        readings.ForceMeshUpdate();
    }
}
