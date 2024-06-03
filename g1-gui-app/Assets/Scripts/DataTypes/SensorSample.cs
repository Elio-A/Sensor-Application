using System;
using UnityEngine;

[Serializable]
public class SensorSample
{
    public float value;
    public string sampleDateTime;

    public SensorSample Clone()
    {
        SensorSample clonedSample = new SensorSample();
        clonedSample.value = value;
        clonedSample.sampleDateTime = sampleDateTime;

        return clonedSample;
    }
}
