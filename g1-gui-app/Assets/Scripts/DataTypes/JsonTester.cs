using System.Collections;
using System.Collections.Generic;
using System.Linq;
using Unity.VisualScripting;
using UnityEngine;

public class JsonTester : MonoBehaviour
{
    public List<VirtualSensorData> virtualSensor;
    public string jsonTestString;
    public bool runTest;
    public string jsonFromSensor;
    // Start is called before the first frame update
    void Start()
    {

    }

    // Update is called once per frame
    void Update()
    {
        if (runTest)
        {
            runTest = false;
            Test();
        }

    }
    void Test()
    {
        StartCoroutine(TestCoroutine());
    }

    IEnumerator TestCoroutine()
    {
        virtualSensor.Add(Deserialize(jsonTestString));
        yield return new WaitForSeconds(2);
        jsonFromSensor = Serialize(virtualSensor.ToArray()[0]);
    }
    VirtualSensorData Deserialize(string json)
    {
        VirtualSensorData newSensor = JsonUtility.FromJson<VirtualSensorData>(json);
        return newSensor;

    }

    string Serialize(VirtualSensorData newSensor)
    {
        string json = JsonUtility.ToJson(newSensor);
        return json;
    }
}
