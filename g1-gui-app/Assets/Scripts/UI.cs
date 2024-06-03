using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class UI : MonoBehaviour
{
    public DataPlotter dataPlotter;
    public TextMeshProUGUI textMeshPro;
    public TMP_InputField inputField;

    public void Start()
    {
        textMeshPro.text = "Increment Rate: " + dataPlotter.secondsPerIncrement.ToString("F2");
        inputField.onValueChanged.AddListener(OnInputValueChanged);
    }

    public void Update()
    {
        textMeshPro.text = "Increment Rate: " + dataPlotter.secondsPerIncrement.ToString("F2");
    }

   private void OnInputValueChanged(string inputValue)
    {
        if (dataPlotter != null)
        {
            if (float.TryParse(inputValue, out float newSamplingRate))
            {
                dataPlotter.SetSamplingRate(newSamplingRate);
            }
        }
    }

    void OnDestroy()
    {
        inputField.onValueChanged.RemoveListener(OnInputValueChanged);
    }
}
