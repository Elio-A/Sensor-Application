using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.IO;

public class CSVWriter
{
    public string fileName = "data.csv";
    
    // Save the CSV file
    public void Save(SensorSample[] samples)
    {
        // Determine the path to the file
        string filePath = Path.Combine(Application.dataPath, fileName);

        // Create a StreamWriter to write to the file
        StreamWriter outStream = File.CreateText(filePath);

        // Loop through the row data and write each row to the file
        foreach (SensorSample data in samples)
        {
            string line = string.Join(",", data.value, data.sampleDateTime);
            outStream.WriteLine(line);
        }

        // Close the StreamWriter
        outStream.Close();
    }
}