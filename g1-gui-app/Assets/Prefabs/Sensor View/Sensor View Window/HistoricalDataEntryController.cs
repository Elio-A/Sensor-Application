using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class HistoricalDataEntryController : MonoBehaviour
{

    public VirtualSensorData _sensor;
    [SerializeField]
    public TextMeshProUGUI location;
    [SerializeField]
    public TextMeshProUGUI _start;
    [SerializeField]
    public TextMeshProUGUI _end;
    [SerializeField]
    public TextMeshProUGUI type;
    [SerializeField]
    public TextMeshProUGUI sensorName;
    [SerializeField]
    private Image panel;

    public void Initialize(VirtualSensorData sensor, Color color, string start, string end)
    {
        _sensor = sensor;
        sensorName.text = _sensor.nickName;
        sensorName.ForceMeshUpdate();
        _start.text = start;
        _start.ForceMeshUpdate();
        _end.text = end;
        _end.ForceMeshUpdate();
        type.text = _sensor.type;
        type.ForceMeshUpdate();
        location.text = _sensor.location;
        location.ForceMeshUpdate();
        panel.color = color;
    }
}
