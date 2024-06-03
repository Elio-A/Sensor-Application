using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class TechTicketListEntryController : MonoBehaviour
{
    private TicketObject _ticketObject;
    [SerializeField]
    private GameObject _ticketWindowPrefab;
    [SerializeField]
    private TextMeshProUGUI _date;
    [SerializeField]
    private TextMeshProUGUI _ticketID;

    public void Initialize(TicketObject ticket)
    {
        _ticketObject = ticket;
        _ticketID.text = "Ticket " + _ticketObject.ticketID.ToString();
        _ticketID.ForceMeshUpdate();
        _date.text = _ticketObject.assignedTimeSlot + " : 00";
        _date.ForceMeshUpdate();
    }

    public void OnEntryClicked()
    {
        GameObject gO = WindowContainer.Instance.LoadWindow(_ticketWindowPrefab);
        gO.GetComponent<TicketWindowInfoController>().Initialize(_ticketObject);
    }
}
