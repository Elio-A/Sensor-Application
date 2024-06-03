using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class GraphLineRendererController : MonoBehaviour
{
    public RectTransform viewPort; // Assign the viewport's RectTransform
    private LineRenderer lineRenderer;
    private List<Vector3> visiblePoints = new List<Vector3>();
    private string sensorId;
    private bool initialized;


    // Update is called once per frame
    void Update()
    {
        if (!initialized) return;

        UpdateVisiblePoints();
        UpdateLineRenderer();
    }
    public void Initialize(string sensorId, Color color)
    {
        lineRenderer = GetComponent<LineRenderer>();
        this.sensorId = sensorId;
        Color lightenedColor = LightenColor(color);
        lineRenderer.material.color = lightenedColor;
        initialized = true;
    }

    private Color LightenColor(Color color, float amount = 0.2f)
    {
        // Lighten the color by the specified amount
        return new Color(
            Mathf.Clamp01(color.r + amount),
            Mathf.Clamp01(color.g + amount),
            Mathf.Clamp01(color.b + amount),
            color.a // Preserve the alpha value
        );
    }
    private void UpdateVisiblePoints()
    {
        visiblePoints.Clear();
        Rect viewPortRect = new Rect(viewPort.rect.min + (Vector2)viewPort.position, viewPort.rect.size);
        bool lastWasMissing = false;

        foreach (var entry in GraphSampleEntryContoller.allEntries)
        {
            if (string.Compare(entry.sensorId, sensorId) != 0 || entry == null) continue;

            Vector3 screenPoint = RectTransformUtility.WorldToScreenPoint(null, entry.marker.position);
            if (viewPortRect.Contains(screenPoint))
            {
                Vector3 position = new Vector3(entry.GetComponentInChildren<SampleMarkerController>().transform.position.x,
                                               entry.GetComponentInChildren<SampleMarkerController>().transform.position.y,
                                               -10);

                if (entry.actualValue == -1)
                {
                    if (!lastWasMissing && visiblePoints.Count > 0)
                    {
                        // Add a point at the previous position but with Z = 1000
                        Vector3 previous = visiblePoints[visiblePoints.Count - 1];
                        visiblePoints.Add(new Vector3(previous.x, previous.y, 1000));
                    }

                    // Add the current point with Z = 1000
                    position.z = 1000;
                    lastWasMissing = true;
                }
                else
                {
                    if (lastWasMissing)
                    {
                        // Add a point at the current position but with Z = 1000
                        visiblePoints.Add(new Vector3(position.x, position.y, 1000));
                    }
                    lastWasMissing = false;
                }

                visiblePoints.Add(position);
            }
        }
    }


    private void UpdateLineRenderer()
    {
        lineRenderer.positionCount = visiblePoints.Count;
        for (int i = 0; i < visiblePoints.Count; i++)
        {
            lineRenderer.SetPosition(i, visiblePoints[i]);
        }
    }
}
