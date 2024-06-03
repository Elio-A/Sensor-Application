using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using framework;

public class ExampleWindowScript : IWindow
{
    [SerializeField]
    private int id;
    public override void Close()
    {
        ExampleWindowFactory.RemoveWindowFromList(id);
        base.Close();
    }
    public override void Show()
    {
        id = ExampleWindowFactory.NextID;
       // GetComponent<ExampleWindowActions>().operationsState.text = "Hello There!";
        base.Show();
    }
    public override void Test()
    {

        base.Test();
    }
    public int GetID()
    {
        return id;
    }
}
