using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class ConfigureSensorController : MonoBehaviour
{
    [SerializeField]
    private TextMeshProUGUI sensorID;
    [SerializeField]
    private TextMeshProUGUI sensorName;
    [SerializeField]
    private TextMeshProUGUI sensorDescription;
    [SerializeField]
    private TextMeshProUGUI sensorType;
    [SerializeField]
    private TextMeshProUGUI sensorLocation;
    [SerializeField]
    private TextMeshProUGUI sensorSamplingRate;
    [SerializeField]
    private TextMeshProUGUI max;
    [SerializeField]
    private TextMeshProUGUI min;
    [SerializeField]
    private TMP_InputField inputSensorName;
    [SerializeField]
    private TMP_InputField inputSensorDescription;
    [SerializeField]
    private TMP_InputField inputSensorType;
    [SerializeField]
    private TMP_InputField inputSensorLocation;
    [SerializeField]
    private TMP_InputField inputSensorSamplingRate;
    [SerializeField]
    private TMP_InputField inputMax;
    [SerializeField]
    private TMP_InputField inputMin;
    private VirtualSensorData _sensorData;

    public void CloseButton()
    {
        Destroy(gameObject);
    }

    public void Initialize(VirtualSensorData sensor)
    {
        _sensorData = sensor;
        sensorID.text = _sensorData.name;
        sensorID.ForceMeshUpdate();
        sensorName.text = _sensorData.nickName;
        sensorName.ForceMeshUpdate();
        sensorDescription.text = _sensorData.description;
        sensorDescription.ForceMeshUpdate();
        sensorLocation.text = _sensorData.location;
        sensorLocation.ForceMeshUpdate();
        sensorType.text = _sensorData.type;
        sensorType.ForceMeshUpdate();
        sensorSamplingRate.text = _sensorData.sampleRate.ToString("0.00");
        sensorSamplingRate.ForceMeshUpdate();
        max.text = _sensorData.max.ToString("0.00");
        max.ForceMeshUpdate();
        min.text = _sensorData.min.ToString("0.00");
        min.ForceMeshUpdate();
    }

    public void OnChageButtonClick()
    {
        if(!(inputSensorName.text == ""))
        {
            _sensorData.nickName = inputSensorName.text;
        }

        if (!(inputSensorDescription.text == ""))
        {
            _sensorData.description = inputSensorDescription.text;
        }

        if (!(inputSensorType.text == ""))
        {
            _sensorData.type = inputSensorType.text;
        }

        if (!(inputSensorLocation.text == ""))
        {
            _sensorData.location = inputSensorLocation.text;
        }

        if (!(inputSensorSamplingRate.text == ""))
        {
            _sensorData.sampleRate = float.Parse(inputSensorSamplingRate.text);
        }

        if (!(inputMax.text == ""))
        {
            _sensorData.max = float.Parse(inputMax.text);
        }

        if (!(inputMin.text == ""))
        {
            _sensorData.min = float.Parse(inputMin.text);
        }

        Initialize(_sensorData);
        Promise<string> s = new Promise<string>();
        StartCoroutine(CommunicationsPortal.Instance.ChangeSensorValue(_sensorData, s));
    }
}
