using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class AdminTicketInfoWidowController : MonoBehaviour
{
    private TicketObject _ticketObject;
    [SerializeField]
    private TextMeshProUGUI _ticketID;
    [SerializeField]
    private TextMeshProUGUI _fieldNotes;
    [SerializeField]
    private TextMeshProUGUI _sensorName;
    [SerializeField]
    private TextMeshProUGUI _sensorLocation;
    [SerializeField]
    private TextMeshProUGUI _assignedTo;
    public void CloseButton()
    {
        Destroy(gameObject);
    }

    public void Initialize(TicketObject ticketObject)
    {
        _ticketObject = ticketObject;
        _ticketID.text = "Ticket " + ticketObject.ticketID.ToString();
        _ticketID.ForceMeshUpdate();
        _fieldNotes.text = ticketObject.fieldNotes;
        _fieldNotes.ForceMeshUpdate();
        _sensorName.text = ticketObject.sensorInfo.name;
        _sensorName.ForceMeshUpdate();
        _sensorLocation.text = ticketObject.sensorInfo.location;
        _sensorLocation.ForceMeshUpdate();
        _assignedTo.text = ticketObject.assignedUserID;
        _assignedTo.ForceMeshUpdate();
    }

}
