using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

namespace framework
{
    public abstract class IWindow : MonoBehaviour, IPointerClickHandler
    {
        static List<IWindow> AllWindows;
        private CanvasGroup canvasGroup;
        private bool isActiveWindow;

        [Tooltip("While app is running in the editor, set this to true in the inspector to test the window during runtime.")]
        public bool testWindow;
        protected virtual void Awake()
        {
            RectTransform rectTransform = GetComponent<RectTransform>();

            try
            {
                GetComponentInChildren<WindowTitleBar>().ParentWindow(rectTransform);
            }
            catch { }
            try
            {
                GetComponentInChildren<CloseButton>().GetComponent<Button>().onClick.AddListener(Close);
            }
            catch { }


            canvasGroup = GetComponent<CanvasGroup>();
            if (canvasGroup == null)
            {
                canvasGroup = gameObject.AddComponent<CanvasGroup>();
            }
            HideAllChildGraphics();
            ChekcList();
        }


        protected virtual void Update()
        {
            if (Input.GetKeyDown(KeyCode.Escape) && isActiveWindow)
            {
                Close();
            }
            if (testWindow)
            {
                testWindow = false;
                Test();
            }
        }
        private void ChekcList()
        {
            if (AllWindows == null)
                AllWindows = new List<IWindow>();
            AllWindows.Add(this);
        }
        public void SetActiveWindow()
        {
            foreach (IWindow window in AllWindows)
            {
                if (window != this)
                    window.isActiveWindow = false;
                isActiveWindow = true;
            }
            transform.SetAsLastSibling();
        }
        public virtual void Close()
        {
            Destroy(gameObject);
        }
        public virtual void Show()
        {
            ShowAllChildGraphics();
        }
        public void HideAllChildGraphics()
        {
            canvasGroup.alpha = 0f;
            canvasGroup.interactable = false;
            canvasGroup.blocksRaycasts = false;
        }
        public void ShowAllChildGraphics()
        {
            canvasGroup.alpha = 1f;
            canvasGroup.interactable = true;
            canvasGroup.blocksRaycasts = true;
        }
        public void OnPointerClick(PointerEventData eventData)
        {
            SetActiveWindow();
        }
        private void OnDestroy()
        {
            AllWindows.Remove(this);
        }
        public virtual void Test()
        {
            Show();
        }
    }
}
