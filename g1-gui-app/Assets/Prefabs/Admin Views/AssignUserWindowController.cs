using System.Collections;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;

public class AssignUserWindowController : MonoBehaviour
{
    public static List<UserEntryController> userEntryControllers = new List<UserEntryController>();
    private bool found = false;
    private List<UserObject> _allUsers;
    private List<UserObject> _assignedUsers;
    private string _sensorID;
    [SerializeField]
    private GameObject _userEntryPrefab;
    [SerializeField]
    private RectTransform container;
    public void CloseButton()
    {
        Destroy(gameObject);
        userEntryControllers.Clear();
    }

    public void Initialize(string sensorID, List<UserObject> allUsers, List<UserObject> assignedUsers)
    {
        _allUsers = allUsers;
        _assignedUsers = assignedUsers;
        _sensorID = sensorID;
        foreach(var user in allUsers)
        {
            foreach (var userObject in assignedUsers)
            {
                if(user.userName == userObject.userName)
                {
                    found = true;
                    break;
                }
            }
            GameObject gO = Instantiate(_userEntryPrefab, container);
            gO.GetComponent<UserEntryController>().Initialize(user, found);
            found = false;
        }
    }

    public void OnSaveButtonClick()
    {
        List<string> users = new List<string>();
        foreach(var user in userEntryControllers)
        {
            if (user.isActive)
            {
               users.Add(user._user.userName);
            }
        }

        Promise<string> s = new Promise<string>();
        string[] sendUsers = users.ToArray();
        StartCoroutine(CommunicationsPortal.Instance.AssignUsers(_sensorID, sendUsers, s));
    }

}
