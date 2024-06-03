using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;
using System.Linq;

namespace framework
{
    public class WindowTitleBar : MonoBehaviour, IBeginDragHandler, IDragHandler, IPointerClickHandler
    {
        private Vector2 _offset;
        private RectTransform _windowRectTransform;

        public void ParentWindow(RectTransform parentWindow)
        {
            _windowRectTransform = parentWindow;
        }

        public void OnBeginDrag(PointerEventData eventData)
        {
            SetActiveWindow();
            _offset = (Vector2)_windowRectTransform.position - eventData.position;
        }

        public void OnDrag(PointerEventData eventData)
        {
            SetActiveWindow();
            _windowRectTransform.position = eventData.position + _offset;
        }
        public void OnPointerClick(PointerEventData eventData)
        {
            SetActiveWindow();
            if (eventData.clickCount == 2)  // Checks for double click
            {
                PutOnScreen();
            }

        }
        private void SetActiveWindow()
        {
            _windowRectTransform.GetComponent<IWindow>().SetActiveWindow();
        }
        private void PutOnScreen()
        {
            _windowRectTransform.anchoredPosition = Vector2.zero;
        }
    }
}
