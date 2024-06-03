using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;
using UnityEngine.SceneManagement;

namespace framework
{
    public class LoginButton : MonoBehaviour
    {
        Button _button;

        private void Awake()
        {
            _button = GetComponent<Button>();
        }
        void Start()
        {
            _button.onClick.AddListener(LoginButtonPressed);
        }

        void LoginButtonPressed()
        {
            LoginManager.Logout();
        }

    }
}