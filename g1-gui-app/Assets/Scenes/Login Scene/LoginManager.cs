using Communications;
using framework;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class LoginManager : MonoBehaviour
{
    private static LoginManager _instance;
    private LoginPrefabController _loginPrefabController;
    [SerializeField]
    private GameObject _loginPrefab;
    private bool _initialized = false;
    private string _userID;
    private GameObject login;
    public static string UserID
    {
        get
        {          
            return _instance._userID;
        }
    }
    public static bool IsConnected
    {
        get
        {
            return _instance._initialized;
        }
    }
    private void Awake()
    {
        _instance = this;
    }
    private void Start()
    {
        _loginPrefabController = LoginSceneWindowContainer.Instance.LoadWindow(_loginPrefab).GetComponent<LoginPrefabController>();
    }
    public void OnConnectionEstablished()
    {
        _initialized = true;
        Debug.Log("SERVER ON");
    }
    public void OnConnectionLost()
    {
        _initialized = false;        
        Debug.Log("SERVER OFF");
    }
    public static bool Login(SceneController.Scenes scene)
    {
        bool success = true;
        if (!TestingManager.InTestMode)
        {
            try
            {
             //   CommunicationsManager.StartCommunications();
            }
            catch
            {
                success = false;
            }
        }
        if (success)
        {
            _instance.SelectScene(scene);
        }
        _instance._userID = "totally real user";
        return success;
    }
    public static bool Login(SceneController.Scenes scene, string userId)
    {
        _instance.SelectScene(scene);
        _instance._userID = userId;
        return true;
    }

    public static void Logout()
    {
        _instance.UnloadAll();
        _instance._loginPrefabController = LoginSceneWindowContainer.Instance.LoadWindow(_instance._loginPrefab).GetComponent<LoginPrefabController>();

    }

    private void SelectScene(SceneController.Scenes scene)
    {
        switch ((int)scene)
        {
            case 1:
                SceneController.LoadResearcherScene();
                break;
            case 2:
                SceneController.LoadTechnicianScene();
                break;
            case 3:
                SceneController.LoadAdministratorScene();
                break;
        }
    }
    private void UnloadAll()
    {
        StartCoroutine(SceneController.UnloadAll());
    }
    private void ShowLoginFields()
    {
        login = LoginSceneWindowContainer.Instance.LoadWindow(_loginPrefab);
    }
    public static Promise<bool> attemptLogin(string username, string password)
    {
        Promise<bool> success = new Promise<bool>();
        Promise<string> user = new Promise<string>();
        _instance.StartCoroutine(_instance.VerifyUser(username, password, user, success));
        return success;
    }
    private IEnumerator VerifyUser(string username,  string password, Promise<string> user, Promise<bool> success)
    {
        yield return CommunicationsPortal.Instance.VerifyLoginDetails(username, password, user);
        yield return new WaitUntil(() => user.done);
        loadScene(user.value, username, success);

    }

    private void loadScene(string userType, string username, Promise<bool> success)
    {
        userType = userType.ToLower();
        Debug.Log("ESER TYPE IS: "+userType);
        if (userType == "researcher")
        {
            SceneController.LoadResearcherScene();
            _instance._userID = username;
            success.value = true;
        }
        else if (userType == "head researcher")
        {
            SceneController.LoadAdministratorScene();
            _instance._userID = username;
            success.value = true;
        }
        else if (userType == "technician")
        {
            SceneController.LoadTechnicianScene();
            _instance._userID = username;
            success.value = true;
        }
        else
        {
            Debug.Log("Invalid login credentials");
            success.value = false;
        }
        success.done = true;
    }

}
