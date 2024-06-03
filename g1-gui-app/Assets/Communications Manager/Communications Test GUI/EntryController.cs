using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.Events;

public class EntryController : MonoBehaviour
{
    public TextMeshProUGUI buttonName;
    UnityAction<string, string> _toDo;
    string _name;
    string _data;
    public void Initialize(string name, string data, UnityAction<string, string> toDo)
    {
        _name = name;
        _data = data;
        _toDo = toDo;
        buttonName.text = _name;        
        buttonName.ForceMeshUpdate();
    }
    public void OnClick()
    {
        _toDo.Invoke(_name, _data);
    }

}
