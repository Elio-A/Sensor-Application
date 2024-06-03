using framework;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;
public class PopUpWindowController : IWindow
{
    public TextMeshProUGUI title;
    public TextMeshProUGUI message;
    public void DisplayMessage(string title,string message)
    {
        this.title.text = title;
        this.title.ForceMeshUpdate();
        this.message.text = message;
        this.message.ForceMeshUpdate();
    }
    public override void Test()
    {
        DisplayMessage("Just a Test", "This is just a test message for testing, because clearly you are testing. Now I am just trying to fill this with text. You really could of stopped reading this long ago. If your still reading please stop. Really you shouldn't be reading this.");
        base.Test();
    }
}
