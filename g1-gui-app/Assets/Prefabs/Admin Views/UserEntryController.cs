using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;


public class UserEntryController : MonoBehaviour
{
    [SerializeField]
    public Toggle toggle;
    public UserObject _user;
    public bool isActive;

    private void Awake()
    {
        toggle.onValueChanged.AddListener(OnEntryClicked);
    }
    public void Initialize(UserObject user, bool assigned)
    {
        _user = user;
        toggle.GetComponentInChildren<Text>().text = user.userName;
        toggle.isOn = assigned;
        AssignUserWindowController.userEntryControllers.Add(this);
    }

    private void OnEntryClicked(bool value)
    {
        isActive = toggle.isOn;
    }
}
