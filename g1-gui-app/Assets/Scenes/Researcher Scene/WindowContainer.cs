using framework;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.UIElements;


public class WindowContainer : IWindowContainer
{
    public static WindowContainer Instance;
    private UnityEngine.UI.Button liveView;
    private UnityEngine.UI.Button historicalView;
    bool pressed;
    private void Awake()
    {
        Instance = this;
    }
}
