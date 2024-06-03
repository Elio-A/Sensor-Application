using framework;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;
using System.Text;

public class ExportWindow : IWindow
{
    [SerializeField]
    TMP_InputField field;




    public void Initalize(VirtualSensorData[] sensors)
    {
        string data = "";
        foreach (var sensor in sensors)
        {
            data += GenerateCSV(sensor);
            data += "\n\n\n";
        }

        field.text = data;
        Show();
    }


    public string GenerateCSV(VirtualSensorData sensorData)
    {
        StringBuilder csvContent = new StringBuilder();

        // Adding metadata
        csvContent.AppendLine("name," + sensorData.name);
        // ... add other metadata as needed

        // Skip 3 rows
        csvContent.AppendLine();
        csvContent.AppendLine();
        csvContent.AppendLine();

        // Adding samples data
        csvContent.AppendLine("sampleDateTime, value");
        foreach (var sample in sensorData.samples)
        {
            csvContent.AppendLine($"{sample.sampleDateTime}, {sample.value}");
        }
        return csvContent.ToString();
    }

}
