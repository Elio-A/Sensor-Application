using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using framework;

public class RecievedContainer : MonoBehaviour
{
    public static RecievedContainer Instance;
    public RectTransform entryContainer;
    public GameObject entryPrefab;
    public GameObject windowPrefab;

    public static Command recieved;

    private void Awake()
    {
        Instance = this;
    }
    public void MessageRecieved(Command command)
    {
        recieved = command;
        Debug.Log("ContainerTriggered!!!!!!!!!!!!!" + command.message);
        TriggerEntry();
    }
    public void TriggerEntry()
    {
        GameObject gO = Instantiate(entryPrefab, entryContainer);
        gO.GetComponent<EntryController>().Initialize(recieved.clientName, recieved.message, OpenWindow);
    }
    public void OpenWindow(string name, string message)
    {
        GameObject gO = Instantiate(windowPrefab, LoginSceneWindowContainer.Instance.transform);
        gO.GetComponent<WindowController>().Load(name, message);
    }

}
