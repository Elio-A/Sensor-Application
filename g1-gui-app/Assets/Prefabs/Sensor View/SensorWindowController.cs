using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;
using framework;
using System;

public class SensorWindowController : IWindow
{
    private VirtualSensorData _sensorData;
    [SerializeField]
    GameObject _sensorLiveViewPrefab;
    [SerializeField]
    GameObject _sensorHistoricalFieldPrefab;
    [SerializeField]
    private TextMeshProUGUI _name;
    [SerializeField]
    private TextMeshProUGUI _description;
    [SerializeField]
    private TextMeshProUGUI _location;
    [SerializeField]
    private TextMeshProUGUI _state; 
    [SerializeField]
    private TextMeshProUGUI _type;
    [SerializeField]
    private TextMeshProUGUI _rate;

    public void Initialize(VirtualSensorData sensorData)
    {
        _sensorData = sensorData;

        _name.text = _sensorData.name;
        _name.ForceMeshUpdate();
        _description.text = _sensorData.description;
        _description.ForceMeshUpdate();
        _location.text = _sensorData.location;
        _location.ForceMeshUpdate();
        _state.text = _sensorData.state;
        _state.ForceMeshUpdate();
        _type.text = _sensorData.type;
        _type.ForceMeshUpdate();
        _rate.text = _sensorData.sampleRate.ToString("0.00");
        _rate.ForceMeshUpdate();

        base.Show();
    }

    public void OnHistoricViewButtonPress()
    {
        GameObject gO = WindowContainer.Instance.LoadWindow(_sensorHistoricalFieldPrefab);

    }
    public void OnLiveViewButtonPress()
    {
        GameObject gO = WindowContainer.Instance.LoadWindow(_sensorLiveViewPrefab);
        
    }


    public override void Test()
    {
        VirtualSensorData sensorData = new()
        {
            name = "Test Sensor",
            description = "This is a test sensor for testing during any test that you might test while testing the test for a test. Words, words, words." +
            "This is a test sensor for testing during any test that you might test while testing the test for a test. Words, words, words. ",
            location = "A Place not far from here but nowheres near over there",
            state = "California",
            type = "The kind that senses things",
        };

        Initialize(sensorData); 
    }

}
