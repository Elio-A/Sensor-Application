using System.Collections;
using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.UIElements;

public class ResearcherSceneManager : MonoBehaviour
{
    [SerializeField]
    private SensorListController sensorListController;
    [SerializeField]
    private GameObject liveViewWindowPrefab;
    [SerializeField]
    private GameObject historicalQueryWindowPrefab;
    public static List<SensorEntryController> controllerList;
    

    void Start()
    {
        controllerList = new List<SensorEntryController>();
        StartCoroutine(GetSensorData());
    }

    public void liveViewButtonPressed()
    {

        List<VirtualSensorData> sensors = new List<VirtualSensorData>();
        foreach(var sensor in controllerList)
        {
            if (sensor.isSelected)
            {
                sensors.Add(sensor._sensorData);
            }
        }
        GameObject gO = WindowContainer.Instance.LoadWindow(liveViewWindowPrefab);
        gO.GetComponent<SensorViewWindowController>().Initialize(sensors.ToArray());
    }
    public void historicalViewButtonPressed()
    {
        List<VirtualSensorData> sensors = new List<VirtualSensorData>();
        foreach (var sensor in controllerList)
        {
            if (sensor.isSelected)
            {
                sensors.Add(sensor._sensorData);
            }
        }
        GameObject gO = WindowContainer.Instance.LoadWindow(historicalQueryWindowPrefab);
        gO.GetComponent<HistoricalParameterWindowController>().Initialize(sensors.ToArray());
    }

    IEnumerator GetSensorData()
    {
        yield return new WaitForSeconds(1.0f);
        Promise<VirtualSensorData[]> result = new Promise<VirtualSensorData[]>();
        yield return CommunicationsPortal.Instance.GetSensorList(LoginManager.UserID, result);
        VirtualSensorData[] list = result.value;
        sensorListController.Initialize(list);
    }
}
