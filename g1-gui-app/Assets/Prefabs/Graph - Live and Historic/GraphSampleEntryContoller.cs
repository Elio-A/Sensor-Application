using System.Collections.Generic;
using UnityEngine;

public class GraphSampleEntryContoller : MonoBehaviour
{
    private const float MAX_HEIGHT = 840;
    private const float MIN_SCALED_VALUE = 20;
    private static float _scale;
    private static float _offset;

    public float actualValue;
    public float scaledValue;
    public string date;

    public static List<GraphSampleEntryContoller> allEntries = new();

    public RectTransform marker;

    public string sensorId;

    private void Awake()
    {
        allEntries.Add(this);
    }

    public void Initialize(SensorSample sample, string sensorName)
    {
        actualValue = sample.value;
        sensorId = sensorName;
        date = sample.sampleDateTime.ToString();
        SetScale();
        AssignValue();
        SetMarkerPosition();

        foreach (var entry in allEntries)
        {
            if (entry == this|| entry==null) continue;
            entry.UpdateVPos();
        }
    }

    public void UpdateVPos()
    {
        AssignValue();
        SetMarkerPosition();
    }

    void SetScale()
    {
        float minVal = Mathf.Infinity;
        float maxVal = Mathf.NegativeInfinity;
        foreach (var entry in allEntries)
        {
            if (entry.actualValue == -1) continue; // Skip entries with -1
            if (entry.actualValue > maxVal)
                maxVal = entry.actualValue;
            if (entry.actualValue < minVal)
                minVal = entry.actualValue;
        }

        _offset = minVal;
        _scale = (MAX_HEIGHT - MIN_SCALED_VALUE) / (maxVal - minVal);
    }

    void AssignValue()
    {
        if (actualValue != -1)
        {
            scaledValue = ActualValue_2_ScaledValue(actualValue);
        }
        else
        {
            scaledValue = -1; // Assign -1 directly for missing data
        }
    }

    float ActualValue_2_ScaledValue(float actualValue)
    {
        float normalizedValue = actualValue - _offset;
        return normalizedValue * _scale + MIN_SCALED_VALUE;
    }

    void SetMarkerPosition()
    {
        marker.anchoredPosition = new Vector2(marker.anchoredPosition.x, scaledValue);
    }

}
