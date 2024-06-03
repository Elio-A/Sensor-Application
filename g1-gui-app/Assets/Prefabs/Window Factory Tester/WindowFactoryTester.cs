using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WindowFactoryTester : MonoBehaviour
{
    public GameObject window;
    public bool testMe;

    private void Update()
    {
        if (testMe)
        {
            testMe = false;
            GameObject tempWindow = LoginSceneWindowContainer.Instance.LoadWindow(window);
        }
    }
}
