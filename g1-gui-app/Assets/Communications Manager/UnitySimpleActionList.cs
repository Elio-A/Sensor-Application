using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;

public class UnitySimpleActionList
{
    private List<UnityAction> actions = new List<UnityAction>();

    public void Subscribe(UnityAction newAction)
    {
        if (newAction != null && !actions.Contains(newAction))
        {
            actions.Add(newAction);
        }
    }

    public void Unsubscribe(UnityAction existingAction)
    {
        if (existingAction != null && actions.Contains(existingAction))
        {
            actions.Remove(existingAction);
        }
    }

    public void Trigger()
    {
        foreach (UnityAction action in actions)
        {
            try
            {
                action?.Invoke();
            }
            catch { }
        }
    }
}


