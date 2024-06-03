using System.Collections;
using UnityEngine;
using UnityEngine.Networking;

namespace Communications
{
    public class ForwardingManager : MonoBehaviour
    {
        private string serverUrl = "http://127.0.0.1:8265";
        private int port;

        public void InitializeComms(int port)
        {
            this.port = port;
            StartCoroutine(TestComms(port));
        }

        public void SendCommand(string message, Promise<Command> result)
        {
            SendMessageToForwardingApp(message, result);
        }
        private void SendMessageToForwardingApp(string message, Promise<Command> result)
        {
            Command cmd = new Command();
            cmd.clientName = "UnityClient";
            cmd.clientPort = port.ToString();
            cmd.message = message;

            string json = JsonUtility.ToJson(cmd);

            UnityWebRequest request = UnityWebRequest.Put(serverUrl + "/receiveFromGui", json);
            request.method = UnityWebRequest.kHttpVerbPOST;
            request.SetRequestHeader("Content-Type", "application/json");

            StartCoroutine(SendMessageCoroutine(request, result));
        }

        private IEnumerator SendMessageCoroutine(UnityWebRequest request, Promise<Command> result)
        {
            yield return request.SendWebRequest();

            if (request.result == UnityWebRequest.Result.Success)
            {
                Debug.Log("Message sent successfully.");

                string jsonResponse = request.downloadHandler.text;
                Command responseCommand = JsonUtility.FromJson<Command>(jsonResponse);
                //RecievingManager.OnRequestReceived(responseCommand);
                Debug.Log("Received response command: " + responseCommand.message);
                Command temp = responseCommand; 
                try
                {
                    responseCommand = JsonUtility.FromJson<Command>(temp.message);        
                }
                catch
                {
                    responseCommand = temp;
                }
                if (string.IsNullOrWhiteSpace(responseCommand.message))
                    result.value = temp;
                else
                    result.value = responseCommand;
            }
            else
            {
                Debug.Log("Error sending message: " + request.error +"\nResult Text:"+ request.result);
            }
            Debug.Log("\n\nResult Text:" 
                + result.value + "\n\n");

            result.done = true;
        }

        private IEnumerator TestComms(int port)
        {
            yield return null;            
         
            SendMessageToForwardingApp("Test Message from Unity", new Promise<Command>(new Command()));
        }
    }
}
