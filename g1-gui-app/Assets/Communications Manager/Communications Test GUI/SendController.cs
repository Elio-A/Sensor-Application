using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class SendController : MonoBehaviour
{
    public TMP_InputField message;
    public Button button;

    public void SendMessageButtonClick()
    {
        Promise<Command> result = new Promise<Command>();
        StartCoroutine(SendMessageCoroutine(message.text, result));
    }

    private IEnumerator SendMessageCoroutine( string message, Promise<Command> result)
    {
        CommunicationsManager.SendCommand(message, result);

        yield return new WaitUntil(() => result.done);
        RecievedContainer.Instance.MessageRecieved(result.value);

    }

}
