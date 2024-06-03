using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class WindowController : MonoBehaviour
{
    public TMP_InputField message;
    public TextMeshProUGUI title;

    public void OnClick()
    {
        Destroy(gameObject);
    }
    public void Load(string title, string message)
    {
        this.title.text = title;
        this.title.ForceMeshUpdate();
        this.message.text = message;
        this.message.ForceLabelUpdate();
    }

    public void LoadTitle(string title)
    {
        this.title.text = title;
    }
    public void LoadMessage(string message) 
    { 
        this.message.text = message;
    }
}
