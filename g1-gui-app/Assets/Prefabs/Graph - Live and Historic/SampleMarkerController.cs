using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI; // Include this if you are using a UI Image component

public class SampleMarkerController : MonoBehaviour
{
    private Image image; 

    private void Awake()
    {
        image = GetComponent<Image>();
    }

    public void SetColour(Color color)
    {
        if (image != null)
        {
            image.color = color; // Set the color of the UI Image
        }
    }
}
