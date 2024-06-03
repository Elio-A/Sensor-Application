using UnityEngine;
using UnityEngine.Events;
using System.Collections.Generic;
using System;

public class UnityActionList<T>
{

    private List<UnityAction<T>> actions = new List<UnityAction<T>>();

    public void Subscribe(UnityAction<T> newAction)
    {
        if (newAction != null && !actions.Contains(newAction))
        {
            actions.Add(newAction);
        }
    }

    public void Unsubscribe(UnityAction<T> existingAction)
    {
        if (existingAction != null && actions.Contains(existingAction))
        {
                actions.Remove(existingAction);
        }
    }

    public void Trigger(T parameter)
    {
        foreach (UnityAction<T> action in actions)
        {
            try
            {
                action?.Invoke(parameter);
            }
            catch { }
        }
    }
}
