using System.Collections.Generic;
using System.Linq;
using TMPro;
using UnityEngine;

public class GraphContainerController : MonoBehaviour
{

    [SerializeField]
    private GameObject _sampleEntryPrefab;
    [SerializeField]
    private RectTransform _container;
    [SerializeField]
    private GraphLineRendererController lineController;
    public bool test;

    int numSamples;
    public static float entrySize = 50;
    public static float markerScale = .5f;

    [SerializeField]
    private List<Color> colors = new List<Color> {
        Color.red, Color.green, Color.blue, Color.yellow, Color.cyan, Color.magenta
    };
    private static int currentColorIndex = 0;
    private Color thisColor;
    public string sensorId;

    private void Update()
    {
        if (test)
        {
            test = false;
            Test();
        }

    }

    private void Test()
    {
        VirtualSensorData tester = SensorGenerator.GetRandomSensor(50, 1,10);
        InitializeGraphHistoric(tester.samples, tester.name);
    }
    public Color InitializeGraphHistoric(SensorSample[] samples, string sensorId)
    {
        this.sensorId = sensorId;
        thisColor = colors[currentColorIndex];
        lineController.Initialize(sensorId, thisColor);

        int sampleCount = samples.Length;
        int step = Mathf.Max(1, sampleCount / 100); // Calculate step size, avoid division by zero

        for (int i = 0; i < sampleCount; i += step)
        {
            AddEntry(samples[i], sensorId);
        }

        currentColorIndex = (currentColorIndex + 1) % colors.Count;
        return thisColor;
    }
    public Color InitializeGraphLive(SensorSample[] samples, string sensorId)
    {
        this.sensorId = sensorId;
        thisColor = colors[currentColorIndex];
        lineController.Initialize(sensorId, thisColor);

        int sampleCount = samples.Length;

        // Start from an index that ensures only the last 100 samples are included
        int startIndex = Mathf.Max(0, sampleCount - 100);

        for (int i = startIndex; i < sampleCount; i++)
        {
            AddEntry(samples[i], sensorId);
        }

        currentColorIndex = (currentColorIndex + 1) % colors.Count;
        return thisColor;

    }


    public void AddEntry(SensorSample sample, string sensorId, bool isLive = false)
    {
        if (isLive)
            while (_container.childCount >= 100)
            {
                GameObject toGo = _container.GetChild(0).gameObject;
                
                // Remove the first (oldest) child
                Destroy(_container.GetChild(0).gameObject);
                numSamples--;
            }
        numSamples++;
        GameObject gO = Instantiate(_sampleEntryPrefab, _container);
        GraphSampleEntryContoller entryController = gO.GetComponent<GraphSampleEntryContoller>();
        entryController.Initialize(sample, sensorId);
        entryController.GetComponentInChildren<SampleMarkerController>().SetColour(thisColor);
        if (numSamples > 27)
            SetSampleEntrySize(GraphSampleEntryContoller.allEntries);
    }

    private void SetSampleEntrySize(List<GraphSampleEntryContoller> entries)
    {
        if (entries.Count == 0) return;
        int num = entries.Count(x => string.Compare(x.name, entries[0].name) == 0 && x != null);
        // Calculate the width of each entry
        float entryWidth = _container.rect.width / num;

        foreach (var entry in entries)
        {
            if (entry == null) continue;
            // Update the width of the entry's RectTransform
            RectTransform entryRect = entry.GetComponent<RectTransform>();
            entryRect.sizeDelta = new Vector2(entryWidth * 3f, entryRect.sizeDelta.y);

            // Find the SampleMarker in the children of the entry and update its scale
            SampleMarkerController marker = entry.GetComponentInChildren<SampleMarkerController>();
            float scale = entryWidth / 50;
            if (marker != null) 
            {
                marker.transform.localScale = new Vector3(scale, scale, marker.transform.localScale.z);
            }
        }
    }

    private void OnDestroy()
    {
        GraphSampleEntryContoller.allEntries.Clear();
    }
}
