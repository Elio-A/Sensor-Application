using framework;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using TMPro;
using UnityEngine;

public class GraphMasterController : MonoBehaviour
{

    public static GraphMasterController Instance;

    public static VirtualSensorData[] currentData;

    [SerializeField]
    TextMeshProUGUI topYAxis;
    [SerializeField]
    TextMeshProUGUI middleYAxis;
    [SerializeField]
    TextMeshProUGUI bottomYAxis;

    [SerializeField]
    TextMeshProUGUI minTime;
    [SerializeField]
    TextMeshProUGUI maxTime;
    
    [SerializeField]
    private GameObject graphContainerPrefab;
    [SerializeField]
    private Transform containerParent;

    private List<GraphContainerController> graphObjects = new();

    public bool test;
    public bool withNeg;
    private void Awake()
    {
        Instance = this;
    }
    private void Start()
    {
        RemoveNullEntries(GraphSampleEntryContoller.allEntries);

    }
    private void Update()
    {
        RemoveNullEntries(GraphSampleEntryContoller.allEntries);

        if (test)
        {
            test = false;
            Test(withNeg);
        }
        SetAxisText();

    }
    public void RemoveNullEntries(List<GraphSampleEntryContoller> entries)
    {
       entries.RemoveAll(item => item ==null|| item.gameObject == null);
    }
    private void Test(bool withNeg)
    {
        if (withNeg)
        {
            IntitializeHistoricGraphs(SensorGenerator.GetRandomSensorArray(3, 2315, 100, 200, true));
        }
        else
            IntitializeHistoricGraphs(SensorGenerator.GetRandomSensorArray(3, 2315, 100, 200));
    }
    public List<Color> IntitializeHistoricGraphs(VirtualSensorData[] sensors)
    {
        currentData = sensors;
        List<Color> result = new List<Color>();
        foreach (VirtualSensorData sensor in sensors)
        {
            GraphContainerController graphObject =  Instantiate(graphContainerPrefab, containerParent).GetComponent< GraphContainerController>();
            graphObjects.Add(graphObject);
            result.Add(graphObject.InitializeGraphHistoric(sensor.samples, sensor.name));
        }
        return result;
    }
    public List<Color> IntitializeLiveGraphs(VirtualSensorData[] sensors)
    {
        List<Color> result = new List<Color>();

        foreach (VirtualSensorData sensor in sensors)
        {
            GraphContainerController graphObject = Instantiate(graphContainerPrefab, containerParent).GetComponent<GraphContainerController>();
            graphObjects.Add(graphObject);
            result.Add(graphObject.InitializeGraphLive(sensor.samples, sensor.name));
        }
        return result;

    }
    public void UpdateLiveGraphs(VirtualSensorData[] sensors)
    {
        bool mismatched = false;
        int[] ii = new int[sensors.Length];
        for(int i = 0; i< sensors.Length; i++)
        {
            ii[i] = -1;
        }
        for (int sensorIndex = 0; sensorIndex < sensors.Length; sensorIndex++)
        {
            VirtualSensorData sensor = sensors[sensorIndex];

            // Use LINQ to find the corresponding graph object
            GraphContainerController graphObject = graphObjects.FirstOrDefault(g => g.sensorId == sensor.name);

            if (graphObject != null)
            {
                int graphObjectIndex = graphObjects.FindIndex(g => g.sensorId == graphObject.sensorId); 

                if (!ii.Contains(graphObjectIndex))
                {
                    graphObject.AddEntry(sensor.samples[0], sensor.name, true);
                    ii[sensorIndex] = graphObjectIndex;
                }
                else
                {
                    mismatched = true;
                    int j = 0;
                    while (ii.Contains(j)) j++;
                    graphObjects[j].AddEntry(sensor.samples[0], sensor.name, true);
                    ii[sensorIndex] = j;
                }
            }
            else
            {
                mismatched = true;
                int j = 0;
                while (ii.Contains(j)) j++;
                graphObjects[j].AddEntry(sensor.samples[0], sensor.name, true);
                ii[sensorIndex] = j;
            }
        }

        if (mismatched)
        {
           //DebugManager.DisplayLog("Sensor Mismatch!", "There has been a data mismatch.\nGraph will still display data but it may not be correct.");
        }
    }


    public void SetAxisText()
    {
        if (GraphSampleEntryContoller.allEntries.Count < 1) return;

        // Filter out entries with actualValue of -1
        var validEntries = GraphSampleEntryContoller.allEntries.Where(entry => entry.actualValue != -1).ToList();

        if (validEntries.Count == 0) return; // Return if there are no valid entries

        // Sort the valid entries by actualValue
        validEntries.Sort((entry1, entry2) => entry1.actualValue.CompareTo(entry2.actualValue));

        // Set min, mid, and max x-axis values
        float minx = validEntries.First().actualValue;
        float maxx = validEntries.Last().actualValue;
        float midx = (minx + maxx) / 2;

        // Set min and max y-axis values (assuming there is a DateTime property in SampleEntryContoller)
        string miny = GraphSampleEntryContoller.allEntries.First().date;
        string maxy = GraphSampleEntryContoller.allEntries.Last().date;

        topYAxis.text = maxx.ToString();
        middleYAxis.text = "" + midx.ToString();
        bottomYAxis.text = "" + minx.ToString();
        minTime.text = miny;
        maxTime.text = maxy;
    }

    private void OnDestroy()
    {
        Instance = null;
        currentData = null;
    }
}
