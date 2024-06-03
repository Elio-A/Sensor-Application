using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class AdminTicketListEntryController : MonoBehaviour
{
    private TicketObject _ticketObject;
    [SerializeField]
    private GameObject _ticketAdminWindowPrefab;
    [SerializeField]
    private TextMeshProUGUI _ticketStatus;
    [SerializeField]
    private TextMeshProUGUI _ticketID;

    public void Initialize(TicketObject ticket)
    {
        _ticketObject = ticket;
        _ticketID.text = "Ticket " + _ticketObject.ticketID.ToString();
        _ticketID.ForceMeshUpdate();
        _ticketStatus.text = _ticketObject.ticketState;
        _ticketStatus.ForceMeshUpdate();
    }

    public void OnTicketButtonClicked()
    {
        GameObject gO = WindowContainer.Instance.LoadWindow(_ticketAdminWindowPrefab);
        gO.GetComponent<AdminTicketInfoWidowController>().Initialize(_ticketObject);
    }

    public void OnDeleteButtonKick()
    {        
        Promise<string> s = new Promise<string>();
        StartCoroutine(CommunicationsPortal.Instance.DeleteTicket(_ticketObject.ticketID.ToString(), s));
        Destroy(gameObject);
    }
}
