using System;
using Unity.Burst.Intrinsics;

[Serializable]
public class Command
{
    public string clientName;
    public string clientPort;
    public string message;

    public Command Clone()
    {
        Command clone = new Command();
        clone.clientName = clientName;
        clone.clientPort = clientPort;
        clone.message = message;

        return clone;
    }
}
