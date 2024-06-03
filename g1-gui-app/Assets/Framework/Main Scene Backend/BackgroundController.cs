using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class BackgroundController : MonoBehaviour
{
    Image _background;
    private void Awake()
    {
        _background = GetComponent<Image>();
    }
    void Update()
    {
        bool sceneIsLoaded = false;
        for (int i = 1; i < SceneManager.sceneCount; i++)
        {
            Scene scene = SceneManager.GetSceneAt(i);
            if (scene.isLoaded)
            {
                sceneIsLoaded = true;
            }
        }
        ShowBackground(!sceneIsLoaded);

    }
    private void ShowBackground(bool show)
    {
        if (_background.enabled != show)
        {
            _background.enabled = show;
        }
    }
}
