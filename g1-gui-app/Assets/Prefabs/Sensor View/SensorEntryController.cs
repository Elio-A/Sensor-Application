using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;
using System.Linq;

public class SensorEntryController : MonoBehaviour
{
    [SerializeField]
    private GameObject sensorViewPrefab;
    [SerializeField]
    private Toggle _toggle;
    public VirtualSensorData _sensorData;
    public bool isSelected;
    [SerializeField]
    private TextMeshProUGUI _name;
    
    private void Awake()
    {
        _toggle.onValueChanged.AddListener(OnEntryClicked);
    }
    public void Initialize(VirtualSensorData sensorData, bool isSelected = false)
    {
        _sensorData = sensorData;
        _name.text = _sensorData.nickName;
        ResearcherSceneManager.controllerList.Add(this);
        StartCoroutine(SetSelected(isSelected));
       // _toggle.ForceMeshUpdate();
    }

    private IEnumerator SetSelected(bool selected)
    {
        yield return new WaitForFixedUpdate();
        yield return new WaitForEndOfFrame();
        _toggle.isOn = selected;
    }

    public void OnEntryClicked(bool value)
    {
        if (_toggle.isOn)
        {
            if (ResearcherSceneManager.controllerList.Count(x => x.isSelected) > 2)
                _toggle.isOn = false;
            else
                isSelected = _toggle.isOn;

        }
        else
        {
            if (ResearcherSceneManager.controllerList.Count(x => x.isSelected) < 2)
                _toggle.isOn = true;
            else
                isSelected = _toggle.isOn;
        }




        //  GameObject gO = WindowContainer.Instance.LoadWindow(sensorViewPrefab);
        // gO.GetComponent<SensorWindowController>().Initialize(_sensorData);
    }

    public void OnButtonClick()
    {
        GameObject gO = WindowContainer.Instance.LoadWindow(sensorViewPrefab);
        gO.GetComponent<SensorWindowController>().Initialize(_sensorData);
    }

}
