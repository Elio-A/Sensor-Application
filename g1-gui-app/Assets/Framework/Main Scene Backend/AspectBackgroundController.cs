using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

namespace framework
{
    public class AspectBackgroundController : MonoBehaviour
    {
        void Start()
        {
            gameObject.GetComponent<Image>().enabled = false;
        }

    }
}
