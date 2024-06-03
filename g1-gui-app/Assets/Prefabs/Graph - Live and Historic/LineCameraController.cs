using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LineCameraController : MonoBehaviour
{
    private Camera targetCamera;
    private Canvas parentCanvas;
    private RectTransform prefabBounds;

    void Start()
    {
        prefabBounds = transform.parent.GetComponent<RectTransform>();
        // Find the parent Canvas
        parentCanvas = GetComponentInParent<Canvas>();
        if (parentCanvas == null)
        {
            Debug.LogError("CameraSizeAdjuster: No Canvas found in parent hierarchy.");
        }
        targetCamera = GetComponent<Camera>();
    }

    void Update()
    {
        if (parentCanvas != null)
        {
            // Calculate the scale factor based on the Canvas scale
            float scaleFactor = parentCanvas.transform.localScale.x * prefabBounds.localScale.x; // Assuming uniform scaling


            // Adjust the camera's orthographic size
            targetCamera.orthographicSize = 437f * scaleFactor;
        }
    }
}
