using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ExamplePopupMessenger : MonoBehaviour
{
    public float waitTime = .3f;
    void Start()
    {
        StartCoroutine(TestPopup());

    }

    // Update is called once per frame
    void Update()
    {
        
    }

    IEnumerator TestPopup()
    {
        yield return new WaitForSeconds(waitTime);

        DebugManager.DisplayLog("Example Popup", "You waited "+waitTime.ToString("0.00")+" seconds to see this message");
        yield return new WaitForSeconds(1);
        DebugManager.DisplayLog("Superfluous Popup", "Thats "+waitTime+" seconds you will never get back! MWHAHAHHA");
    }
}
