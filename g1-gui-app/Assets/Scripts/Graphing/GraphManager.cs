using UnityEngine;
using System.Collections.Generic;

public class GraphManager : MonoBehaviour
{
    public float readInterval = 2f; // Read data every n seconds
    public float printInterval = 5f; // Print data every m seconds

    private float timer1 = 0f;
    private float timer2 = 0f;  

    public TextAsset csvFile;
    public List<float> data = new List<float>();
    public int i = 0;
    private CSVReader csvReader;

    private void Start()
    {
        csvReader = GetComponent<CSVReader>();
        ReadCSV();
    }

    private void Update()
    {
        if(i<data.Count)
        {
            timer1 += Time.deltaTime;
            timer2 += Time.deltaTime;
            if (timer1 >= readInterval)
            {
                Debug.Log("CSV Value: " + data[i]);
                timer1=0;
                i++;
            }
            if (timer2 >= printInterval)
            {
                Debug.Log("Graphed Value: " + data[i]);
                timer2=0;
            }
        }
        else
        {
            i=0;
        }
        
    }

    public void ReadCSV()
    {
        string[] lines = csvFile.text.Split('\n');
        foreach (var line in lines)
        {
            float dataP;
            if (float.TryParse(line.Trim(), out dataP))
            {
                data.Add(dataP);
            }
        }
    }
}
