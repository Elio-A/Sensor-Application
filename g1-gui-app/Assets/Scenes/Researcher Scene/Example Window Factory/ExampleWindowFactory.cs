using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Linq;

public class ExampleWindowFactory : MonoBehaviour
{

    [SerializeField]
    private GameObject mathOperationsPrefab;
    [SerializeField]
    private List<GameObject> mathOperationsWindows = new();

    private static ExampleWindowFactory _instance;

    private int nextID = 0;

    public static int NextID
    {
        get 
        {
            return _instance.nextID++;
        }
    }

    private void Awake()
    {
        _instance = this;
    }
    public void NewWindowButtonPress()
    {
        StartCoroutine(InstantiateWindow());
    }

    private IEnumerator InstantiateWindow()
    {
        GameObject newWindow = WindowContainer.Instance.LoadWindow(mathOperationsPrefab);
        mathOperationsWindows.Add(newWindow);
        newWindow.transform.localScale = Vector3.one;
        yield return null;
        newWindow.GetComponent<ExampleWindowScript>().Show();

    }
    public static void RemoveWindowFromList(int id)
    {
        GameObject toRemove = null;
        foreach (GameObject window in _instance.mathOperationsWindows) 
        {
            int thisID = window.GetComponent<ExampleWindowScript>().GetID();
            if (thisID == id)
            {
                toRemove = window;
                break;
            }
        }
        if (toRemove != null)
        {
            _instance.mathOperationsWindows.Remove(toRemove);
        }
    }
}
