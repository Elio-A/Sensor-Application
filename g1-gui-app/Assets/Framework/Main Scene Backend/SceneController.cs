using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class SceneController : MonoBehaviour
{
    private static SceneController _instance;
    public GameObject canvas;
    public enum Scenes
    {
        LoginScene = 0,
        ResearcherScene = 1,
        TechnicianScene = 2,
        AdministratorScene = 3
    }

    private void Awake()
    {
        if (_instance == null)
        {
            _instance = this;
        }
        else
        {
            Destroy(gameObject);
        }
    }

    public static void LoadResearcherScene()
    {
        _instance.LoadNewScene(Scenes.ResearcherScene);
    }

    public static void LoadTechnicianScene()
    {
        _instance.LoadNewScene(Scenes.TechnicianScene);
    }

    public static void LoadAdministratorScene()
    {
        _instance.LoadNewScene(Scenes.AdministratorScene);
    }

    private  void LoadNewScene(Scenes targetScene)
    {
        StartCoroutine(ChangeSceneAsync(targetScene));
    }

    private IEnumerator ChangeSceneAsync(Scenes targetScene)
    {
        // Unload previous scene (other than Login)
        for (int i = 1; i < SceneManager.sceneCount; i++)
        {
            Scene scene = SceneManager.GetSceneAt(i);
            if (scene.isLoaded)
            {
                yield return SceneManager.UnloadSceneAsync(scene.name);
            }
        }

        // Load target scene
        AsyncOperation asyncLoad = SceneManager.LoadSceneAsync(targetScene.ToString(), LoadSceneMode.Additive);

        while (!asyncLoad.isDone)
        {
            yield return null;
        }

        // Ensure Login scene is always on top
        SceneManager.SetActiveScene(SceneManager.GetSceneByName(Scenes.LoginScene.ToString()));
    }
    public static IEnumerator UnloadAll()
    {
        for (int i = 1; i < SceneManager.sceneCount; i++)
        {
            Scene scene = SceneManager.GetSceneAt(i);
            if (scene.isLoaded)
            {
                yield return SceneManager.UnloadSceneAsync(scene.name);
            }
        }
    }
}


