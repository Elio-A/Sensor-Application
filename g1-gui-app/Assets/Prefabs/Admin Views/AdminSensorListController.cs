using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AdminSensorListController : MonoBehaviour
{
    [SerializeField]
    GameObject adminSensorEntryPrefab;
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

    public void CloseButton()
    {
        Destroy(gameObject);
    }

    public void Initialize(VirtualSensorData[] sensors)
    {
        foreach (var sensor in sensors)
        {
            AddEntry(sensor);
        }
    }

    private void AddEntry(VirtualSensorData sensorData)
    {

        GameObject gO = Instantiate(adminSensorEntryPrefab, container);       
        StartCoroutine(GetSensorStatus(sensorData.name, sensorData, gO.GetComponent<AdminSensorListEntryController>()));


    }

    private IEnumerator GetSensorStatus(string name, VirtualSensorData sensorData,  AdminSensorListEntryController contoller)
    {
        string status = "";

        Promise<string> ticketStat = new Promise<string>();

        yield return StartCoroutine(CommunicationsPortal.Instance.GetTicketStatus(name, ticketStat));
        yield return new WaitUntil(() => ticketStat.done);

       // Command doubleWrappedOops = (Command)JsonUtility.FromJson(ticketStat.value, typeof(Command));
        status = ticketStat.value;

        contoller.Initialize(sensorData, status);
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
                });
        }
        Initialize(sensors.ToArray());
    }
}
