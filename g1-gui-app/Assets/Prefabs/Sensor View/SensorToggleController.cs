using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class SensorToggleController : MonoBehaviour
{
  
    [SerializeField]
    private Toggle _name;
    private VirtualSensorData _sensorData;

    public void Initialize(VirtualSensorData sensorData)
    {
        _sensorData = sensorData;
        _name.GetComponentInChildren<Text>().text = _sensorData.nickName;
    }

}
