using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AdministratorSceneManager : MonoBehaviour
{
    [SerializeField]
    private GameObject listViewPrefab;
    [SerializeField]
    private GameObject ticketViewPrefab;
    [SerializeField]
    private GameObject configureViewPrefab;
    [SerializeField]
    private GameObject aggregateWindowPrefab;
    public void OnViewSensorListButtonClick()
    {
        StartCoroutine(GetSensorList());
    }

    public void OnViewTicketListButtonClick()
    {
        StartCoroutine(GetTicketList());
    }

    public void OnConfigureSensorButtonClick()
    {
        GameObject gO = WindowContainer.Instance.LoadWindow(configureViewPrefab);
    }

    public void OnCreateAggregateButtonClick()
    {
        GameObject gO = WindowContainer.Instance.LoadWindow(aggregateWindowPrefab);
    }

    IEnumerator GetTicketList()
    {
        yield return new WaitForSeconds(1.0f);
        Promise<TicketObject[]> result = new Promise<TicketObject[]>();
        yield return CommunicationsPortal.Instance.GetTicketList(result);
        TicketObject[] list = result.value;
        GameObject gO = WindowContainer.Instance.LoadWindow(ticketViewPrefab);
        gO.GetComponent<TechTicketListController>().Initialize(list);
    }

    IEnumerator GetSensorList()
    {
        yield return new WaitForSeconds(1.0f);
        Promise<VirtualSensorData[]> result = new Promise<VirtualSensorData[]>();
        yield return CommunicationsPortal.Instance.GetSensorList(LoginManager.UserID, result);
        VirtualSensorData[] list = result.value;
        GameObject gO = WindowContainer.Instance.LoadWindow(listViewPrefab);
        gO.GetComponent<AdminSensorListController>().Initialize(list);

    }
}
