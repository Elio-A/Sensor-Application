using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ExportManager : MonoBehaviour
{

    [SerializeField]
    private GameObject exportPrefab;

    private Button b;

    void Start()
    {
        b = GetComponent<Button>();
        b.onClick.AddListener(OnButtonPress);
        b.interactable = false;

        StartCoroutine(LateStart());
    }
    public IEnumerator LateStart()
    {
        while (GraphMasterController.currentData == null)
        {
            yield return null;
        }

        b.interactable = true;

    }
    public void OnButtonPress()
    {
        StartCoroutine(Init());
    }
    public IEnumerator Init()
    {
        ExportWindow eW = WindowContainer.Instance.LoadWindow(exportPrefab).GetComponent<ExportWindow>();

        yield return new WaitForFixedUpdate();
        yield return new WaitForEndOfFrame();

        eW.Initalize(GraphMasterController.currentData);
    }
}
