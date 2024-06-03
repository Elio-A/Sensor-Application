using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TechTicketListController : MonoBehaviour
{
    [SerializeField]
    GameObject ticketEntryPrefab;
    [SerializeField]
    private Transform container;

    public int testnum;
    public bool test;
    private void Update()
    {
        if (test)
        {
            test = false;
        }
    }

    public void CloseButton()
    {
        Destroy(gameObject);
    }

    public void Initialize(TicketObject[] ticketObjects)
    {
        foreach (var ticket in ticketObjects)
        {
            AddEntry(ticket);
        }
    }

    private void AddEntry(TicketObject ticket)
    {
        GameObject gO = Instantiate(ticketEntryPrefab, container);
        if (gO.TryGetComponent(out TechTicketListEntryController controller))
            controller.Initialize(ticket);
        else if (gO.TryGetComponent(out AdminTicketListEntryController controller2))
            controller2.Initialize(ticket);

    }
}
