using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TechnicianSceneManager : MonoBehaviour
{

    [SerializeField]
    private GameObject schedulePrefab;
    [SerializeField]
    private GameObject ticketViewPrefab;
    public void OnSetScheduleButtonClick()
    {
        GameObject gO = WindowContainer.Instance.LoadWindow(schedulePrefab);
    }

    public void OnViewTicketListButtonClick()
    {
        StartCoroutine(GetTicketList());
    }


    IEnumerator GetTicketList()
    {
        yield return new WaitForSeconds(1.0f);
       Promise<TicketObject[]> result = new Promise<TicketObject[]>();
        yield return CommunicationsPortal.Instance.GetTicketList(LoginManager.UserID, result);
        TicketObject[] list = result.value;
        GameObject gO = WindowContainer.Instance.LoadWindow(ticketViewPrefab);
        gO.GetComponent<TechTicketListController>().Initialize(list);
    }
}
