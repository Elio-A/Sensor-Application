using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ScheduleWindowController : MonoBehaviour
{
    [SerializeField]
    private Toggle[] all;
    private bool[] schedule = new bool[12];
    public void CloseButton()
    {
        Destroy(gameObject);
    }

    private void Start()
    {
        StartCoroutine(GetSchedule());
        
    }

    private IEnumerator GetSchedule()
    {
        Promise<string> schedule = new Promise<string>();
        yield return CommunicationsPortal.Instance.GetSchedule(LoginManager.UserID, schedule);
        string[] result = schedule.value.Split(';');

        for (int i = 0; i < all.Length; i++)
        {
            if (result[i] == "1")
            {
                all[i].isOn = true;
            }
            else
            {
                all[i].isOn = false;
            }
        }

    }

    public void onSaveButtonClick()
    {
        Debug.Log("onSaveButtonClick for ScheduleWindowController ");
        string result = "";
        for (int i = 0; i < schedule.Length; i++)
        {
            schedule[i] = all[i].isOn;
        }
        

        for(int i = 0; i < schedule.Length; i++)
        {
            if (schedule[i] == true)
            {
                result += "1";
            }
            else
            {
                result += "0";
            }
            if(!(i == 11))
            {
                result += ":";
            }
        }

        StartCoroutine(SetSchedule(result));
    }

    private IEnumerator SetSchedule(string message)
    {
        Debug.Log("MUST SEE MESSAGE: " + message);

        Promise<string> s = new Promise<string>();
        yield return CommunicationsPortal.Instance.SendSchedule(LoginManager.UserID, message, s);

        yield return new WaitUntil(() => s.done);
        Debug.Log(s.value);

    }

}
