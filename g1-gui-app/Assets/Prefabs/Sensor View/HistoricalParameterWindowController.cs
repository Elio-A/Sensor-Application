using framework;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class HistoricalParameterWindowController : MonoBehaviour
{ 
    private VirtualSensorData[] _sensorData;
    [SerializeField]
    private GameObject _sensorViewPrefab;
    [SerializeField]
    public TMP_InputField startDate;
    [SerializeField]
    public TMP_InputField endDate;

    public void CloseButton()
    {
        Destroy(gameObject);
    }

    public void Initialize(VirtualSensorData[] sensorData)
    {
        _sensorData = sensorData;
    }

    public void OnViewSensorButtonPressed()
    {
        if(startDate.text == "")
        {

        }
        else
        {
            GameObject gO = WindowContainer.Instance.LoadWindow(_sensorViewPrefab);
            gO.GetComponent<HistoricalSensorViewWidowController>().Initialize(_sensorData, startDate.text, endDate.text);
        }
        
    }
    
}
