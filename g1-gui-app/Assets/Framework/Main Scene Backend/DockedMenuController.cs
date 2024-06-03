using System.Collections;
using System.Collections.Generic;
using UnityEngine;

 namespace framework
{
    public class DockedMenuController : MonoBehaviour
    {
        public static DockedMenuController Instance;
        private Animator _animator;
        public enum DockedMenuState
        {
            PeekMenu,
            ShowMenu,
            HideMenu
        }

        void Awake()
        {
            Instance = this;
            _animator = GetComponent<Animator>();
        }
        private void Start()
        {
            StartCoroutine(BeginAnim());
        }
        public static void ChangeState(DockedMenuState state)
        {
            Instance.PlayAnimation(state.ToString());
        }
        public void PlayAnimation(string animName)
        {
            float blendTime = 0.1f;  // Duration of the blend. Adjust as needed.
            _animator.CrossFade(animName, blendTime);
        }
        private IEnumerator BeginAnim()
        {
            PlayAnimation(DockedMenuState.ShowMenu.ToString());
            yield return new WaitForSeconds(5);
            PlayAnimation(DockedMenuState.HideMenu.ToString());
        }
    }
}

