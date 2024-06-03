using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using Unity.VisualScripting;
using UnityEngine;
using System.Collections;

namespace Communications
{
    public class RecievingManager : MonoBehaviour
    {
        public static UnityActionList<Command> onMessageRevieved = new();
        public static UnitySimpleActionList onStarted = new();
        public static UnitySimpleActionList onStopped = new();

        private bool isRunning;

        private static Command messageRecieved;
        private static Queue<Command> messageRecievedInWaiting = new();

        private static bool triggerOnMessageRecieved;
        private static bool triggerOnStart;
        private static bool triggerOnStop;


        void Update()
        {
            if (isRunning)
            {
                if (triggerOnMessageRecieved)
                {

                        onMessageRevieved.Trigger(messageRecieved.Clone());
                        triggerOnMessageRecieved = false;

                }
                if (triggerOnStart)
                {
                    triggerOnStart = false;
                    onStarted.Trigger();
                }
                if (triggerOnStop)
                {
                    triggerOnStop = false;
                    onStopped.Trigger();
                }
                if (!triggerOnMessageRecieved && messageRecievedInWaiting.Count > 0)
                {
                    messageRecieved = messageRecievedInWaiting.Dequeue();
                    triggerOnMessageRecieved = true;
                }
            }
        }

       
        public static void OnRequestReceived(Command command)
        {    
                if (command != null)
                {
                    Debug.Log($"Received Command from {command.clientName} with message: {command.message}");
                    if (!triggerOnMessageRecieved)
                    {
                        Debug.Log("TRIGGER RECIEVED ACTIONS");
                        triggerOnMessageRecieved = true;
                        messageRecieved = command.Clone();
                    }
                    else
                    {
                        Debug.Log("ENQUEUED");
                        messageRecievedInWaiting.Enqueue(command.Clone());
                    }
                }           
        }

        public void StartServer()
        {
            StartCoroutine(Starter());
        }
        public IEnumerator Starter()
        {
            yield return new WaitForSeconds(2);
            triggerOnStart = true;
            isRunning = true;

        }
        public void StopServer()
        {
            isRunning = false;

            triggerOnStop = true;
        }


        private void OnDisable()
        {
            StopServer();
        }
    }
}
