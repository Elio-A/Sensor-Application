using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SensorViewWindowController : MonoBehaviour
{
    private VirtualSensorData[] _currentSensorData;
    [SerializeField]
    private SensorDataController _sensorDataController;
    [SerializeField]
    private GraphMasterController _graphController;
    private bool first = true;

    public void CloseButton()
    {
        Destroy(gameObject);
        SensorDataController.sensorLiveDataEntries.Clear();
        GraphSampleEntryContoller.allEntries.Clear();
    }

    private void Start()
    {
        BeginRefreshingSample();
    }

    public void Initialize(VirtualSensorData[] sensorData)
    {
        _currentSensorData = sensorData;        
    }

    private void BeginRefreshingSample()
    {
        Promise<VirtualSensorData>[] newestSampleAvailable = new Promise<VirtualSensorData>[_currentSensorData.Length];
        for (int i = 0; i < newestSampleAvailable.Length; i++)
        {
            newestSampleAvailable[i] = new Promise<VirtualSensorData>(new VirtualSensorData());
        }
        StartCoroutine(RetrieveNewestSamples(newestSampleAvailable));
    }

    private IEnumerator RetrieveNewestSamples(Promise<VirtualSensorData>[] sensorSample)// must refactor with webAPI reference
    {
        while (true)
        {
            Debug.Log("Loop started");
            yield return new WaitForSeconds(_currentSensorData[0].sampleRate * .8f);
            List<VirtualSensorData> sensors = new List<VirtualSensorData>();
            for(int i = 0; i < _currentSensorData.Length; i++)
            {
                StartCoroutine(GetSensorData(sensorSample[i], _currentSensorData[i].name));
            }

            for(int i = 0; i<_currentSensorData.Length; i++)
            {          
                yield return new WaitUntil(() => sensorSample[i].done);
                Debug.Log("sample returned");
                Debug.Log("Done! and Good!");
                sensors.Add(sensorSample[i].value);                
                Debug.Log("restarting!");
            }
            VirtualSensorData[] virtualSensorDatas = sensors.ToArray();
            AssignFields(virtualSensorDatas);
        }
    }

    IEnumerator GetSensorData(Promise<VirtualSensorData> sensorSample, string sensorId)
    {
        yield return new WaitForSeconds(1.0f);
        Promise<VirtualSensorData> result = new Promise<VirtualSensorData>();
        yield return StartCoroutine(CommunicationsPortal.Instance.GetCurrentSensorData(sensorId, result));
        sensorSample.value = result.value;
        sensorSample.done = true;
    }

    private void AssignFields(VirtualSensorData[] sensors)
    {
        if (first)
        {
            List<Color> colours = _graphController.IntitializeLiveGraphs(sensors);

            _sensorDataController.Initialize(sensors, colours, false, "", "");
            first = false;
        }
        else
        {
            _sensorDataController.UpdateValues(sensors);
            _graphController.UpdateLiveGraphs(sensors);
        }
    }
}
