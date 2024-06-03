using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;

namespace framework
{
    public class MenuHoverController : MonoBehaviour, IPointerEnterHandler, IPointerExitHandler
    {
        bool _isShown;
        Coroutine hideCoroutine;
        float hideDelay = 5f; // Time to wait before hiding the menu

        public void OnPointerEnter(PointerEventData eventData)
        {
            // If menu isn't shown and mouse enters, show the menu
            if (!_isShown)
            {
                ChangeState(true);
            }

            // If there's an ongoing coroutine to hide the menu, stop it
            if (hideCoroutine != null)
            {
                StopCoroutine(hideCoroutine);
                hideCoroutine = null;
            }
        }

        public void OnPointerExit(PointerEventData eventData)
        {
            // If menu is shown and mouse exits, start the coroutine to hide the menu after a delay
            if (_isShown)
            {
                hideCoroutine = StartCoroutine(HideAfterDelay());
            }
        }

        IEnumerator HideAfterDelay()
        {
            yield return new WaitForSeconds(hideDelay);
            ChangeState(false);
        }

        private void ChangeState(bool show)
        {
            _isShown = show;
            if (show)
                DockedMenuController.ChangeState(DockedMenuController.DockedMenuState.ShowMenu);
            else
                DockedMenuController.ChangeState(DockedMenuController.DockedMenuState.HideMenu);
        }
    }
}
