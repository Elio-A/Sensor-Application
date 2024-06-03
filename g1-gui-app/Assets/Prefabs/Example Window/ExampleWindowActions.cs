using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

public class ExampleWindowActions : MonoBehaviour
{
    public TMP_Dropdown _operator;
    public TMP_InputField numberOne;
    public TMP_InputField numberTwo;
    public TMP_InputField answer;

    public TextMeshProUGUI operationsState;

    public void OperationsButtonPress()
    {
        switch (_operator.value)
        {
            case 0:
                answer.text =  Add(float.Parse(numberOne.text), float.Parse(numberTwo.text)).ToString("0.000");
                break;
            case 1:
                answer.text = Subtract(float.Parse(numberOne.text), float.Parse(numberTwo.text)).ToString("0.000");
                break;
            case 2:
                answer.text = Multiply(float.Parse(numberOne.text), float.Parse(numberTwo.text)).ToString("0.000");
                break;
        }
        operationsState.text = "DONE!";
    }

    private float Add(float num1, float num2)
    {
        float answer = 0;
        answer = num1 + num2;

        return answer;
    }
    private float Subtract(float num1, float num2)
    {
        float answer = num1 - num2;

        return answer;
    }
    private float Multiply(float num1, float num2)
    {
        float answer = 0;
        answer = num1 * num2;

        return answer;
    }

}
