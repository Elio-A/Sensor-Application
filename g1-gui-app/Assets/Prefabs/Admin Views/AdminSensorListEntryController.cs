using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class AdminSensorListEntryController : MonoBehaviour
{
    private VirtualSensorData _sensorData;
    [SerializeField] 
    private GameObject sensorViewPrefab;
    [SerializeField] 
    private GameObject assignGroupsViewPrefab;
    [SerializeField]
    private TextMeshProUGUI sensorId;
    [SerializeField]
    private TextMeshProUGUI sensorStatus;
    [SerializeField]
    private TextMeshProUGUI ticketStatus;


    public void Initialize(VirtualSensorData virtualSensorData, string status)
    {
        _sensorData = virtualSensorData;
        sensorId.text = _sensorData.name;
        sensorId.ForceMeshUpdate();
        sensorStatus.text = _sensorData.state;
        sensorStatus.ForceMeshUpdate();
        ticketStatus.text = status;
        ticketStatus.ForceMeshUpdate();
    }



    public void OnSensorEntryClicked()
    {
        GameObject gO = WindowContainer.Instance.LoadWindow(sensorViewPrefab);
        gO.GetComponent<SensorWindowController>().Initialize(_sensorData);
    }

    public void OnViewGroupsClicked()
    {
        StartCoroutine(GetAllUsers());
    }

    private IEnumerator GetAllUsers()
    {
        yield return new WaitForSeconds(1.0f);
        Promise<UserObject[]> users = new Promise<UserObject[]>();
        Promise<UserObject[]> assignedUsers = new Promise<UserObject[]>();
        
        yield return CommunicationsPortal.Instance.GetUsers(users);
        yield return CommunicationsPortal.Instance.GetUsers(_sensorData.name, assignedUsers);

        yield return new WaitUntil(()=>assignedUsers.done);

        List<UserObject> allUsers = new(users.value);
        List<UserObject> _assignedUsers = new(assignedUsers.value);

        GameObject gO = WindowContainer.Instance.LoadWindow(assignGroupsViewPrefab);
        gO.GetComponent<AssignUserWindowController>().Initialize(_sensorData.name, allUsers,_assignedUsers);
    }


}
