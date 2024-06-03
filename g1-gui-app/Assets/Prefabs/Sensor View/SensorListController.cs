using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SensorListController : MonoBehaviour
{
    [SerializeField]
    GameObject sensorEntryPrefab; 
    [SerializeField]
    private Transform container;

    public int testnum;
    public bool test;
    private void Update()
    {
        if (test)
        {
            test = false;
            Test();
        }
    }

    public void Initialize(VirtualSensorData[] sensors)
    {
        int i = 0;
        foreach (var sensor in sensors)
        {
            AddEntry(sensor, i++);
        }
    }

    private void AddEntry(VirtualSensorData sensorData, int i)
    {
        GameObject gO = Instantiate(sensorEntryPrefab, container);
        gO.GetComponent<SensorEntryController>().Initialize(sensorData, i < 3);
    }
    private void Test()
    {
        List<VirtualSensorData> sensors = new();

        for (int i = 0; i < testnum; i++)
        {
            sensors.Add(
                new()
                {
                    name = "Test Sensor " + i,
                    description = "This is a test sensor for testing during any test that you might test while testing the test " +
                    "for a test. Words, words, words. This is a test sensor for testing during any test that you might test while " +
                    "testing the test for a test. Words, words, words.",
                    location = "A Place not far from here but nowheres near over there",
                    state = "California",
                    type = "The kind that senses things", 
                    sampleRate = 3,
                    samples = new SensorSample[1]
                    {
                        new SensorSample
                        {
                            value = 2.34545f,
                            sampleDateTime = Time.realtimeSinceStartup.ToString()
                        }
                    }
                }) ;
        }
        Initialize(sensors.ToArray());
    }
}
