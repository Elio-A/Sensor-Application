using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;


public class LoginPrefabController : MonoBehaviour
{
    [SerializeField]
    private TMP_InputField username;
    [SerializeField]
    private TMP_InputField password;
    [SerializeField]
    public TextMeshProUGUI message;
    private string userNameDetail;
    private string pass;
    

    public void onLoginButtonClick()
    {
        userNameDetail = username.text.ToLower();
        pass = password.text;
        Promise<bool> success = LoginManager.attemptLogin(userNameDetail, pass);
        StartCoroutine(SuccessEval(success));
    }

    private IEnumerator SuccessEval(Promise<bool> success)
    {
        yield return new WaitUntil(()=>success.done);

        if (success.value)
        {
            DebugManager.DisplayLog("LOGIN SUCCESS", "You have logged in!");
            Destroy(gameObject);
        }
        else
        {
            DebugManager.DisplayLog("LOGIN FAILED", "You have not logged in!\nTry Again, I know you'll get it one of these times:)");
        }
    }
}


