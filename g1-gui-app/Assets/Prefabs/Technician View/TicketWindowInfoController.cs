using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class TicketWindowInfoController : MonoBehaviour
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
    private TMP_Dropdown _status;
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
        if (ticketObject.ticketState == "NotStarted")
        {
            _status.value = 0;
            _status.RefreshShownValue();
        }else if (ticketObject.ticketState == "InProgress")
        {
            _status.value = 1;
            _status.RefreshShownValue();
        }
    }

    public void onSaveButtonClick()
    {
        if (_status.value == 0)
        {
            _ticketObject.ticketState = "NotStarted";
            StartCoroutine(SetTicketStatus(_ticketObject));
        }
        else if (_status.value == 1)
        {
            _ticketObject.ticketState = "InProgress";
            StartCoroutine(SetTicketStatus(_ticketObject));
        }
        else if (_status.value == 2)
        {
            _ticketObject.ticketState = "Completed";
            StartCoroutine(SetTicketStatus(_ticketObject));
        }
    }

    private IEnumerator SetTicketStatus(TicketObject ticket)
    {
        Promise<string> s = new Promise<string>();
        yield return CommunicationsPortal.Instance.SaveTicketStatus(ticket, s);
    }
}
