using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class AggregateDataWindowController : MonoBehaviour
{
    [SerializeField]
    SensorConfigureListController sensorListController;
    [SerializeField]
    private TMP_InputField inputSensorID;
    [SerializeField]
    private TMP_InputField inputSensorName;
    [SerializeField]
    private TMP_InputField inputSensorDescription;
   // [SerializeField]
   // private TMP_InputField inputSensorType;
    [SerializeField]
    private TMP_InputField inputSensorLocation;
  //  [SerializeField]
  //  private TMP_InputField inputSensorSamplingRate;
   // [SerializeField]
   // private TMP_InputField inputMax;
   // [SerializeField]
   // private TMP_InputField inputMin;
    private bool okay;

    void Start()
    {
        StartCoroutine(GetSensorData());
        okay = true;
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
        sensorListController.Initialize(list, true);

    }

    public void OnChageButtonClick()
    {
        if (inputSensorID.text == "")
        {
            okay = false;
        }
        else if (inputSensorName.text == "")
        {
            okay = false;
        }
        else if (inputSensorDescription.text == "")
        {
            okay = false;
        }
  
        else if (inputSensorLocation.text == "")
        {
            okay = false;
        }

       
        if (okay)
        {
            Promise<string> s = new Promise<string>();
            StartCoroutine(CommunicationsPortal.Instance.SendAggregateData(inputSensorID.text, inputSensorName.text, "a", "a", "a", inputSensorDescription.text, "a", s));
        }
        else
        {
            DebugManager.DisplayLog("Alert", "Complete all fields before creating aggregate");
        }

        okay = true;        
    }
}
