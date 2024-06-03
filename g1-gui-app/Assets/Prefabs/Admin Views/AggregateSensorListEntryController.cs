using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class AggregateSensorListEntryController : MonoBehaviour
{
    [SerializeField]
    private TextMeshProUGUI _sensorID;
    [SerializeField]
    private GameObject sensorViewPrefab;
    private VirtualSensorData _sensorData;


    public void Initialize(VirtualSensorData sensorData)
    {
        _sensorData = sensorData;
        _sensorID.text = _sensorData.name;
        _sensorID.ForceMeshUpdate();
    }

    public void OnEntryButtonClicked()
    {
        GameObject gO = WindowContainer.Instance.LoadWindow(sensorViewPrefab);
        gO.GetComponent<SensorWindowController>().Initialize(_sensorData);
    }
}
