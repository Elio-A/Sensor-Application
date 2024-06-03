using framework;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LoginSceneWindowContainer : IWindowContainer
{
    public static LoginSceneWindowContainer Instance;
    private void Awake()
    {
        Instance = this;
    }
}
