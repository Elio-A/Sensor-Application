using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;
using System;
using framework;

public class SensorLiveSampleViewController : IWindow
{
    [Header("\nAssign")]
    private VirtualSensorData _currentSensorData;
    [SerializeField]
    private TextMeshProUGUI _name;
    [SerializeField]
    private TextMeshProUGUI _sampleRate;
    [SerializeField]
    private TextMeshProUGUI _sample;
    [SerializeField]
    private TextMeshProUGUI _sampleTime;

    [Header("\nTesting")]
    public bool test;
    private System.Random rand;

    private readonly VirtualSensorData _testSensor = new()
    {
        name = "Test Sensor 66",
        description = "This is a test sensor for testing during any test that you might test while testing the test " +
                    "for a test. Words, words, words. This is a test sensor for testing during any test that you might test while " +
                    "testing the test for a test. Words, words, words.",
        location = "A Place not far from here but nowheres near over there",
        state = "California",
        type = "The kind that senses things",
        sampleRate = 3
    };

    protected override void Awake()
    {
        rand = new();
        base.Awake();
    }

    protected override void Update()
    {
        if (test)
        {
            test = false;
            _testSensor.samples = new SensorSample[1];
            _testSensor.samples[0] = new()
            {
                value = 9.81f,
                sampleDateTime = Time.realtimeSinceStartup.ToString()
            };
            Initialize(_testSensor);
        }
        base.Update();
    }
    public void Initialize(VirtualSensorData sensorData)
    {
        AssignFields(sensorData);
        BeginRefreshingSample();
        base.Show();
    }
    private void AssignFields(VirtualSensorData sensorData)
    {
        _currentSensorData = sensorData;
        _name.text = _currentSensorData.name;
        _name.ForceMeshUpdate();
        _sampleRate.text = _currentSensorData.sampleRate.ToString("0.00");
        _sampleRate.ForceMeshUpdate();
        _sample.text = sensorData.samples[0].value.ToString("0.000");
        _sample.ForceMeshUpdate();
        _sampleTime.text = sensorData.samples[0].sampleDateTime;
        _sampleTime.ForceMeshUpdate();
    }

    private void BeginRefreshingSample()
    {
        Promise<VirtualSensorData> newestSampleAvailable = new Promise<VirtualSensorData>(_currentSensorData);
        StartCoroutine(RetrieveNewestSample(newestSampleAvailable));
    }

    private IEnumerator RetrieveNewestSample(Promise<VirtualSensorData> sensorSample)// must refactor with webAPI reference
    {
        while (true) 
        {
            Debug.Log("Loop started");
            yield return new WaitForSeconds(_currentSensorData.sampleRate * .8f);
            Debug.Log("wait time reached");
            StartCoroutine(RetrieveTestSample(sensorSample)); // refactor this <<----------------------------- refactor this
            Debug.Log("coroutine begun");
            yield return new WaitUntil(() => sensorSample.done);
            Debug.Log("sample returned");        
            Debug.Log("Done! and Good!");
            AssignFields(sensorSample.value);
            sensorSample = new();
            Debug.Log("restarting!");
        }
    }
    private IEnumerator RetrieveTestSample(Promise<VirtualSensorData> sensorSample)
    {
        yield return null;
        VirtualSensorData sample = new()
        {
            name = _currentSensorData.name,
            description = _currentSensorData.description,
            location = _currentSensorData.location,
            state = _currentSensorData.state,
            type = _currentSensorData.type,
            sampleRate = _currentSensorData.sampleRate
        };
        sample.samples = new SensorSample[1];

        sample.samples[0] = new()
        {
            value = ((float)rand.NextDouble()*10) - .1f,
            sampleDateTime = Time.realtimeSinceStartup.ToString()
        };
        sensorSample.value = sample;
        sensorSample.done = true;
    }

}
