using System.Collections;
using System.Collections.Generic;
using UnityEditor.PackageManager.UI;
using UnityEngine;
using UnityEngine.Profiling;

public class SensorGenerator : MonoBehaviour
{
    private static SensorGenerator _instance;
    private System.Random rand;

    static int sensorNumber = 1;

    private void Awake()
    {
        _instance = this;
        rand = new System.Random();
    }


    public static VirtualSensorData[] GetRandomSensorArray(int numberOfSensors, int numberOfSamples, float max = 1, float min = 0, bool withNegs = false)
    {
        VirtualSensorData[] result = new VirtualSensorData[numberOfSensors];
        for (int i = 0; i < numberOfSensors; i++)
        {
            VirtualSensorData sensor = GetRandomSensor(numberOfSamples, max, min);

            if (withNegs && numberOfSamples > 2)
            {
                int negsLength = numberOfSamples / 3;
                int start = UnityEngine.Random.Range(0, numberOfSamples);
                for (int j = 0; j < negsLength; j++)
                {
                    sensor.samples[(start + j) % numberOfSamples].value = -1;
                }
            }

            result[i] = sensor;
        }

        return result;
    }



    public static VirtualSensorData GetRandomSensor(int numberOfSamples, float max, float min)
    {
        VirtualSensorData generatedSensor = new();

        generatedSensor.name = "Test Sensor " + sensorNumber;

        generatedSensor.description = "This is a test sensor for testing during any test that you might test while testing the test " +
                 "for a test. Words, words, words. This is a test sensor for testing during any test that you might test while " +
                 "testing the test for a test. Words, words, words.";
        generatedSensor.location = "A Place not far from here but nowhere near over there";

        generatedSensor.state = "Active";

        string type = "Energy";
        if (sensorNumber % 2 == 0)
            type = "Water";
        if (sensorNumber % 3 == 0)
            type = "Virtual";

        generatedSensor.type = type;
        generatedSensor.nickName =type + " Sensor " + sensorNumber;
        generatedSensor.sampleRate = 3;
        sensorNumber++;
        List<SensorSample> samples = new List<SensorSample>();

        for (int i = 0; i < numberOfSamples; i++)
        {
            samples.Add(new SensorSample
            {
                // Generate a random value within the specified range
                value = min + ((float)_instance.rand.NextDouble() * (max - min)),
                sampleDateTime = Time.realtimeSinceStartup + ""
            }) ;
        }

        generatedSensor.samples = samples.ToArray();

        return generatedSensor;
    }


}
