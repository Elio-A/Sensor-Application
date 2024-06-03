using System;
using System.Collections;
using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;

public class HistoricalSensorViewWidowController : MonoBehaviour
{

    private VirtualSensorData[] _currentSensorData;
    [SerializeField]
    private SensorDataController _sensorDataController;
    [SerializeField]
    private GraphMasterController _graphController;
    private string _startDate;
    private string _endDate;
    public void CloseButton()
    {
        Destroy(gameObject);
        SensorDataController.sensorLiveDataEntries.Clear();
        GraphSampleEntryContoller.allEntries.Clear();
    }

    private void Start()
    {
        GetSample();
    }

    public void Initialize(VirtualSensorData[] sensorData, string startDate, string endDate)
    {
        _currentSensorData = sensorData;
        _startDate = startDate;
        _endDate = endDate;
    }

    private void GetSample()
    {
        Promise<VirtualSensorData>[] historicalSampleAvailable = new Promise<VirtualSensorData>[_currentSensorData.Length];
        for (int i = 0; i < historicalSampleAvailable.Length; i++)
        {
            historicalSampleAvailable[i] = new Promise<VirtualSensorData>(new VirtualSensorData());
        }
        StartCoroutine(RetrieveHistoricalSample(historicalSampleAvailable));
    }

    private IEnumerator RetrieveHistoricalSample(Promise<VirtualSensorData>[] sensorSample)// must refactor with webAPI reference
    {

        Debug.Log("started");
        List<VirtualSensorData> sensors = new();
        for (int i = 0; i < _currentSensorData.Length; i++)
        {
            StartCoroutine(GetSensorData(sensorSample[i], _currentSensorData[i].name));
        }

        for (int i = 0; i < _currentSensorData.Length; i++)
        {
            yield return new WaitUntil(() => sensorSample[i].done);
            Debug.Log("sample returned");
            Debug.Log("Done! and Good!");
            sensors.Add(sensorSample[i].value);
            Debug.Log("restarting!");
        }
        VirtualSensorData[] virtualSensorDatas = sensors.ToArray();
        virtualSensorDatas = SyncronizeSamples(virtualSensorDatas);
        AssignFields(virtualSensorDatas);
    }

    IEnumerator GetSensorData(Promise<VirtualSensorData> sensorSample, string sensorId)
    {
        yield return new WaitForSeconds(1.0f);
        Promise<VirtualSensorData> result = new Promise<VirtualSensorData>();
        if (_endDate == "")
        {
            yield return CommunicationsPortal.Instance.GetSemiHistoricSensorData(sensorId, _startDate, result);
        }
        else
        {
            yield return CommunicationsPortal.Instance.GetFullHistoricSensorData(sensorId, _startDate, _endDate, result);
        }
        if (result.value == null)
        {
            Destroy(gameObject);
        }
        sensorSample.value = result.value;
        sensorSample.done = true;
    }

    private void AssignFields(VirtualSensorData[] sensors)
    {

        List<Color> colours = _graphController.IntitializeHistoricGraphs(sensors);

        _sensorDataController.Initialize(_currentSensorData, colours, true, _startDate, _endDate);
    }

    private VirtualSensorData[] SyncronizeSamples(VirtualSensorData[] sens)
    {

        int xx = sens[0].samples.Length;
        bool go = false;
        foreach (VirtualSensorData vSD in sens)
        {
            if (vSD.samples.Length != xx)
            {
                go = true;
                break;
            }
        }
        if (!go) return sens;

        // they are different do sync them

        List<VirtualSensorData> tempSensorsList = new();

        float minSamples = Mathf.Infinity;

        for (int i = 0; i < sens.Length; i++)
        {
            if (sens[i].samples.Length < 4)
            {
                DebugManager.DisplayLog("ERRONIOUS REQUEST", "One of the sensors you " +
                    "have selected do not enough samples associated with them and has " +
                    "been removed from your selection.\nSensor Name:\n" + sens[i].nickName);
                sens[i] = null;
                continue;
            }

            if (sens[i].samples.Length < minSamples)
                minSamples = sens[i].samples.Length;
        }
        foreach (VirtualSensorData vSD in sens)
        {
            if (vSD == null) continue;
            VirtualSensorData clonedsen = vSD.Clone();

            tempSensorsList.Add(clonedsen);
            int sampleCount = clonedsen.samples.Length;
            int step = Mathf.Max(1, sampleCount / (int)minSamples);

            List<SensorSample> samples = new List<SensorSample>();
            for (int i = 0; i < sampleCount; i += step)
            {
                samples.Add(vSD.samples[i].Clone());
            }
            clonedsen.samples = samples.ToArray();
        }
        // at this point all sesnros have almost the same number of samples.
        //next we removce the last few from the middle
        minSamples = Mathf.Infinity;
        foreach (VirtualSensorData vSD in tempSensorsList)
        {
            if (vSD.samples.Length < minSamples)
                minSamples = vSD.samples.Length;
        }
        int toRemove;
        foreach (VirtualSensorData vSD in tempSensorsList)
        {
            toRemove = 0;

            if (vSD.samples.Length > minSamples)
                toRemove = vSD.samples.Length - (int)minSamples;
            else
                continue;

            if (toRemove > 0)
            {
                for (int i = vSD.samples.Length / 2, ii = (vSD.samples.Length / 2) - 1, j = 0; j < toRemove; i++, ii--)
                {
                    vSD.samples[i] = null;
                    j++;
                    if (j < toRemove)
                    {
                        vSD.samples[ii] = null;
                        j++;
                    }
                }
            }

            List<SensorSample> samples = new();
            foreach (var s in vSD.samples)
            {
                if (s != null)
                    samples.Add(s);
            }
            vSD.samples = samples.ToArray();
        }
        return tempSensorsList.ToArray();
    }
}
