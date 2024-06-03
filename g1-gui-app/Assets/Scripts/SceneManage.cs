using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class SceneManage : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        if(Input.GetKeyDown(KeyCode.G))
        {
            SceneManager.UnloadSceneAsync("Scene2");
            Scene scene = SceneManager.GetSceneByName("Scene1");
            if (!scene.IsValid() || !scene.isLoaded)
            {
                SceneManager.LoadScene("Scene1", LoadSceneMode.Additive);
            }
        }
        else if(Input.GetKeyDown(KeyCode.H))
        {
            SceneManager.UnloadSceneAsync("Scene1");
            Scene scene = SceneManager.GetSceneByName("Scene2");
            if (!scene.IsValid() || !scene.isLoaded)
            {
                SceneManager.LoadScene("Scene2", LoadSceneMode.Additive);
            }
        }
        
    }

}
