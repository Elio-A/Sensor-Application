using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AdminConfigureController : MonoBehaviour
{
    [SerializeField]
    SensorConfigureListController sensorListController;

    void Start()
    {
        StartCoroutine(GetSensorData());
    }

    public void CloseButton()
    {
        Destroy(gameObject);
    }

    IEnumerator GetSensorData()
    {
        yield return new WaitForSeconds(1.0f);
        Promise<VirtualSensorData[]> result = new Promise<VirtualSensorData[]>();
        yield return CommunicationsPortal.Instance.GetSensorList(LoginManager.UserID, result);
        VirtualSensorData[] list = result.value;
        sensorListController.Initialize(list, false);

    }
}
