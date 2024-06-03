using System;
using UnityEngine;

[Serializable]
public class VirtualSensorData
{
    public string name;
    public string nickName;
    public string location;
    public string description;
    public float sampleRate;
    public float max;
    public float min;
    public string state;
    public string type;
    public SensorSample[] samples;

    public VirtualSensorData Clone()
    {
        VirtualSensorData clonedData = new VirtualSensorData();
        clonedData.name = name;
        clonedData.nickName = nickName;
        clonedData.location = location;
        clonedData.description = description;
        clonedData.sampleRate = sampleRate;
        clonedData.max = max;
        clonedData.min = min;
        clonedData.state = state;
        clonedData.type = type;

        if (samples != null)
        {
            clonedData.samples = new SensorSample[samples.Length];
            for (int i = 0; i < samples.Length; i++)
            {
                // Assuming SensorSample class has a Clone method
                clonedData.samples[i] = samples[i].Clone();
            }
        }

        return clonedData;
    }

    public override string ToString()
    {
        string text = "Name: " + name + "\n"
                    + "Nick Name: " + nickName + "\n"
                    + "Location: " + location + "\n"
                    + "Type: " + type + "\n"
                    + "Description: " + description + "\n"
                    + "Sampling rate: " + sampleRate;

        return text;
    }

}
