using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace framework
{
    public class TestingManager : MonoBehaviour
    {
        public GameObject testingWindow;
        public bool testMode;

        public static bool InTestMode
        {
            get
            {
                return _instance.testMode;
            }
        }

        private GameObject _activeTestWindow;
        private static TestingManager _instance;

        private void Awake()
        {
            _instance = this;
        }
        private void Update()
        {
            if ( Input.GetKeyDown(KeyCode.T) && Input.GetKeyDown(KeyCode.E) && Input.GetKeyDown(KeyCode.S))
                StartTesting();
        }

        private void StartTesting()
        {
            if (_activeTestWindow == null)
            {
                _activeTestWindow = LoginSceneWindowContainer.Instance.LoadWindow(testingWindow);
                _activeTestWindow.GetComponent<IWindow>().Show();
            }
        }

    }
}
