using UnityEngine;
using System.Collections.Generic;
using TMPro;

public class TextTest : MonoBehaviour
{
    public TextAsset csvData;  // TextAsset containing the CSV data
    public float displayInterval = 1f;  // Display interval in seconds
    public GameObject textMeshProPrefab;  // Prefab of the TextMeshPro object

    private List<string> csvContent;  // List to store the CSV content
    private int currentIndex = 0;     // Index to track the current content
    private float displayTimer = 0f;  // Timer to track display intervals

    void Start()
    {
        // Read the CSV data and store its content
        csvContent = ReadCSV(csvData);
    }

    void Update()
    {
        // Increment the timer
        displayTimer += Time.deltaTime;

        // Check if it's time to display content
        if (displayTimer >= displayInterval)
        {
            // Instantiate a new TextMeshPro object
            GameObject newTextObject = Instantiate(textMeshProPrefab, transform.position, Quaternion.identity);
            TextMeshProUGUI textMeshPro = newTextObject.GetComponent<TextMeshProUGUI>();

            // Set the text value to the current content
            textMeshPro.text = csvContent[currentIndex];

            // Increment the index and loop the content if necessary
            currentIndex = (currentIndex + 1) % csvContent.Count;

            // Reset the timer for the next interval
            displayTimer = 0f;
        }
    }

    List<string> ReadCSV(TextAsset csvFile)
    {
        List<string> content = new List<string>();

        if (csvFile != null)
        {
            // Split the CSV data by newline characters to get rows
            string[] lines = csvFile.text.Split('\n');

            foreach (var line in lines)
            {
                // Trim any extra whitespace and add the line to the content list
                content.Add(line.Trim());
            }
        }
        else
        {
            Debug.LogError("No CSV data found.");
        }

        return content;
    }
}
