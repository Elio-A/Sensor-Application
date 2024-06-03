using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace framework
{
    public abstract class IWindowContainer : MonoBehaviour
    {
        public virtual GameObject LoadWindow(GameObject window)
        {
            GameObject gO = Instantiate(window, Vector3.zero, Quaternion.identity, transform); 
            gO.GetComponent<RectTransform>().anchoredPosition = Vector3.zero;
            return gO;
        }
    }
}
