using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class SensorConfigureEntryController : MonoBehaviour
{
    [SerializeField]
    private GameObject sensorViewPrefab;
    [SerializeField]
    private TextMeshProUGUI _name;
    private VirtualSensorData _sensorData;

    public void Initialize(VirtualSensorData sensorData)
    {
        _sensorData = sensorData;
        _name.text = _sensorData.name;
        _name.ForceMeshUpdate();        
    }

    public void OnEntryClicked()
    {
        GameObject gO = WindowContainer.Instance.LoadWindow(sensorViewPrefab);
        gO.GetComponent<ConfigureSensorController>().Initialize(_sensorData);
    }
}
