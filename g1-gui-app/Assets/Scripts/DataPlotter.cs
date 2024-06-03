using UnityEngine;
using TMPro;

public class DataPlotter : MonoBehaviour
{
    public GameObject dataPointPrefab;
    
    public float initialX = 0f;  // Initial X position for the first data point
    public float xIncrement = 10f;  // Increment along the X-axis
    public float secondsPerIncrement = 1.0f;  // Time between each increment
    public float yScale = 1.0f;  // Scale the Y-axis for visualization

    private float nextX = 0f;  // The next X position for the next data point
    private float timer = 0f;
    private CSVReader csvReader;


    void Start()
    {
        csvReader = GetComponent<CSVReader>();
    }


    void CreateDataPoint(Vector3 position)
    {
        GameObject dataPointObject = Instantiate(dataPointPrefab, position, Quaternion.identity);
        position.y *= yScale;
        dataPointObject.transform.position = position;
    }

    public void SetSamplingRate(float newSamplingRate)
    {
        secondsPerIncrement = newSamplingRate;
    }

}
